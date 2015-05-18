package com.guesswhat.server.service.rs.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;

import com.guesswhat.server.persistence.jpa.dao.ImageDAO;
import com.guesswhat.server.persistence.jpa.dao.ImageHolderDAO;
import com.guesswhat.server.persistence.jpa.dao.QuestionDAO;
import com.guesswhat.server.persistence.jpa.entity.Image;
import com.guesswhat.server.persistence.jpa.entity.ImageHolder;
import com.guesswhat.server.persistence.jpa.entity.Question;
import com.guesswhat.server.service.rs.dto.ComposedQuestionDTO;
import com.guesswhat.server.service.rs.dto.ImageDTO;
import com.guesswhat.server.service.rs.dto.ImageType;
import com.guesswhat.server.service.rs.dto.QuestionDTO;
import com.guesswhat.server.service.rs.face.DatabaseService;
import com.guesswhat.server.service.rs.face.ImageService;
import com.guesswhat.server.service.rs.face.QuestionService;

@Path("/questions")
public class QuestionServiceImpl implements QuestionService {

	@Autowired private DatabaseService databaseService;
	@Autowired private ImageService imageService;
	
	@Autowired private QuestionDAO questionDAO;
	@Autowired private ImageHolderDAO imageHolderDAO;
	@Autowired private ImageDAO imageDAO;
	
	@Override
	@RolesAllowed("READER")
	public Response findQuestions() {
		List<Question> questions = questionDAO.findAll();
		List<QuestionDTO> questionDTOList = new ArrayList<QuestionDTO>();
		for (Question question : questions) {
			QuestionDTO questionDTO = new QuestionDTO(question);
			questionDTOList.add(questionDTO);
		}
		
		return Response.ok(questionDTOList).build();
	}

	@Override
	@RolesAllowed("WRITER")
	public Response createQuestion(ComposedQuestionDTO composedQuestionDTO) {
		if (validate(composedQuestionDTO)) {
			Question question = new Question(composedQuestionDTO.getQuestionDTO());
			ImageHolder imageQuestionHolder = null;
			ImageHolder imageAnswerHolder = null;
			if (composedQuestionDTO.getImageQuestionDTO() != null) {
				imageQuestionHolder = buildImageHolder(composedQuestionDTO.getImageQuestionDTO());
			}
			if (imageQuestionHolder != null) {
				question.setImageQuestionId(imageQuestionHolder.getId());
				
				if (composedQuestionDTO.getImageAnswerDTO() != null) {
					imageAnswerHolder = buildImageHolder(composedQuestionDTO.getImageAnswerDTO());					
					if (imageAnswerHolder != null) {
						question.setImageAnswerId(imageAnswerHolder.getId());
					}
				}			
				
				questionDAO.save(question);
				databaseService.incrementVersion();
			}
		}
		
		return Response.ok().build();
	}
	
	private boolean validate(ComposedQuestionDTO composedQuestionDTO) {
		QuestionDTO questionDTO = composedQuestionDTO.getQuestionDTO();
		if (questionDTO.getAnswer1() == null || questionDTO.getAnswer1().isEmpty()
				|| questionDTO.getAnswer2() == null || questionDTO.getAnswer2().isEmpty()
				|| questionDTO.getAnswer3() == null || questionDTO.getAnswer3().isEmpty()
				|| questionDTO.getAnswer4() == null || questionDTO.getAnswer4().isEmpty()
				|| questionDTO.getCorrectAnswer() == null || questionDTO.getCorrectAnswer().isEmpty()) {
					return false;
		}
		
		ImageDTO imageDTO = composedQuestionDTO.getImageQuestionDTO();
		if (!validate(imageDTO)) {
			return false;
		}
		
		imageDTO = composedQuestionDTO.getImageAnswerDTO();
		if (imageDTO != null && !validate(imageDTO)) {
			return false;
		}
		
		return true;
	}

	private boolean validate(ImageDTO imageDTO) {
		if (imageDTO == null || imageDTO.getLdpiImageId() == null || 
				imageDTO.getMdpiImageId() == null || imageDTO.getHdpiImageId() == null
				|| imageDTO.getXhdpiImageId() == null || imageDTO.getXxhdpiImageId() == null) {
			return false;
		}
		
		return true;
	}

	private ImageHolder buildImageHolder(ImageDTO imageDTO) {
		ImageHolder imageHolder = new ImageHolder();
		for (ImageType type : ImageType.values()) {
			byte [] bytes = null;
			switch (type) {
				case XXHDPI: bytes = 	imageDTO.getXxhdpiImageId();
									 	break;
				case XHDPI: bytes = 	imageDTO.getXhdpiImageId();
				 						break;
				case HDPI: bytes = 		imageDTO.getHdpiImageId();
				 						break;
				case MDPI: bytes = 		imageDTO.getMdpiImageId();
				 						break;
				case LDPI: bytes = 		imageDTO.getLdpiImageId();
				 						break;
				default:   return null;
			}
			imageService.buildImageHolder(imageHolder, type.toString(), bytes);
		}
		
		if (imageHolder.isFull()) {
			imageHolderDAO.save(imageHolder);
			return imageHolder;
		}
		
		return null;
	}
	
	@Override
	@RolesAllowed("WRITER")
	public Response downloadQuestion(Long questionId) {
		ComposedQuestionDTO composedQuestionDTO = new ComposedQuestionDTO();
		
		Question question = questionDAO.find(questionId);
		QuestionDTO questionDTO = new QuestionDTO(question);
		composedQuestionDTO.setQuestionDTO(questionDTO);
		
		Long imageHolderQuestionId = question.getImageQuestionId();
		ImageDTO imageDTO = populateImageDTO(imageHolderQuestionId);
		composedQuestionDTO.setImageQuestionDTO(imageDTO);
		
		Long imageHolderAnswerId = question.getImageAnswerId();
		if (imageHolderAnswerId != null) {
			imageDTO = populateImageDTO(imageHolderAnswerId);
			composedQuestionDTO.setImageQuestionDTO(imageDTO);
		}
		
		return Response.ok(composedQuestionDTO).build();
	}
	
	private ImageDTO populateImageDTO(Long imageHolderId) {
		ImageDTO imageDTO = new ImageDTO();
		ImageHolder imageHolder = imageHolderDAO.find(imageHolderId);
		imageDTO.setLdpiImageId(imageService.findImageById(imageHolder.getLdpiImageId()));
		imageDTO.setMdpiImageId(imageService.findImageById(imageHolder.getMdpiImageId()));
		imageDTO.setHdpiImageId(imageService.findImageById(imageHolder.getHdpiImageId()));
		imageDTO.setXhdpiImageId(imageService.findImageById(imageHolder.getXhdpiImageId()));
		imageDTO.setXxhdpiImageId(imageService.findImageById(imageHolder.getXxhdpiImageId()));
		return imageDTO;
	}

	@Override
	@RolesAllowed("READER")
	public Response findQuestion(Long questionId) {
		Question question = questionDAO.find(questionId);
		QuestionDTO questionDTO = new QuestionDTO(question);
		
		return Response.ok(questionDTO).build();
	}

	@Override
	@RolesAllowed("WRITER")
	public Response deleteQuestion(Long questionId) {
		Question question = questionDAO.find(questionId);
		if (question != null) {
			Long imageHolderId = question.getImageQuestionId();
			removeImageHolder(imageHolderId);
			imageHolderId = question.getImageAnswerId();
			removeImageHolder(imageHolderId);
			
			questionDAO.remove(questionId);
			databaseService.incrementVersion();
		}
		
		return Response.ok().build();
	}
	
	@Override
	@RolesAllowed("WRITER")
	public Response deleteQuestions() {
		questionDAO.removeAll();
		databaseService.incrementVersion();
		
		return Response.ok().build();
	}

	private void removeImageHolder(Long imageHolderId) {
		if (imageHolderId != null) {
			ImageHolder imageHolder = imageHolderDAO.find(imageHolderId);
			if (imageHolder != null) {
				removeImage(imageHolder.getLdpiImageId());
				removeImage(imageHolder.getMdpiImageId());
				removeImage(imageHolder.getHdpiImageId());
				removeImage(imageHolder.getXhdpiImageId());
				removeImage(imageHolder.getXxhdpiImageId());
				
				imageHolderDAO.remove(imageHolderId);
			}
		}
	}

	private void removeImage(Long imageId) {
		if (imageId != null) {
			Image image = imageDAO.find(imageId);
			if (image != null) {
				Image secondPart = null;
				if (image.getSecondPart() != null) {
					secondPart = imageDAO.find(image.getSecondPart());
				}
				
				imageDAO.remove(imageId);
				if (secondPart != null) {
					imageDAO.remove(secondPart.getId());
				}
			}
		}
	}

	@Override
	@RolesAllowed("WRITER")
	public Response updateQuestion(QuestionDTO questionDTO) {
		Question question = new Question(questionDTO);
		questionDAO.update(question);
		
		return Response.ok().build();
	}

	public void setDatabaseService(DatabaseService databaseService) {
		this.databaseService = databaseService;
	}

	public void setImageService(ImageService imageService) {
		this.imageService = imageService;
	}

	public void setQuestionDAO(QuestionDAO questionDAO) {
		this.questionDAO = questionDAO;
	}

	public void setImageHolderDAO(ImageHolderDAO imageHolderDAO) {
		this.imageHolderDAO = imageHolderDAO;
	}

	public void setImageDAO(ImageDAO imageDAO) {
		this.imageDAO = imageDAO;
	}
	
}

