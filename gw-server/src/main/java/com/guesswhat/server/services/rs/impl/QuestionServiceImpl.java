package com.guesswhat.server.services.rs.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;

import com.guesswhat.server.persistence.jpa.dao.ImageDAO;
import com.guesswhat.server.persistence.jpa.dao.ImageHolderDAO;
import com.guesswhat.server.persistence.jpa.dao.QuestionDAO;
import com.guesswhat.server.persistence.jpa.dao.QuestionIncubatorDAO;
import com.guesswhat.server.persistence.jpa.entity.Image;
import com.guesswhat.server.persistence.jpa.entity.ImageHolder;
import com.guesswhat.server.persistence.jpa.entity.Question;
import com.guesswhat.server.persistence.jpa.entity.QuestionIncubator;
import com.guesswhat.server.services.rs.dto.QuestionDTO;
import com.guesswhat.server.services.rs.face.DatabaseService;
import com.guesswhat.server.services.rs.face.QuestionService;

@Path("/questions")
public class QuestionServiceImpl implements QuestionService {

	@Autowired private DatabaseService databaseService;
	
	@Autowired private QuestionDAO questionDAO;
	@Autowired QuestionIncubatorDAO questionIncubatorDAO;
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
	public Response createQuestion(QuestionDTO questionDTO) {
		QuestionIncubator questionIncubator = new QuestionIncubator(questionDTO);
		questionIncubatorDAO.save(questionIncubator);
		
		return Response.ok(questionIncubator.getId()).build();
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

	@Override
	@RolesAllowed("WRITER")
	public Response buildQuestion(Long questionId) {
		QuestionIncubator questionIncubator = questionIncubatorDAO.find(questionId);
		
		ImageHolder imageHolderQuestion = null;
		if (questionIncubator.getImageQuestionId() != null) {
			imageHolderQuestion = imageHolderDAO.find(questionIncubator.getImageQuestionId());
		
			ImageHolder imageHolderAnswer = null;
			if (questionIncubator.getImageAnswerId() != null) {
				imageHolderAnswer = imageHolderDAO.find(questionIncubator.getImageAnswerId());
			}
			
			if (imageHolderQuestion.isFull()) {
				Question question = new Question(questionIncubator);
				if (imageHolderAnswer == null || !imageHolderAnswer.isFull()) {
					question.setImageAnswerId(null);
				}
				questionDAO.save(question);
				questionIncubatorDAO.remove(questionIncubator.getId());
				databaseService.incrementVersion();
			}
		}
		
		return Response.ok().build();
	}

	public void setDatabaseService(DatabaseService databaseService) {
		this.databaseService = databaseService;
	}

	public void setQuestionDAO(QuestionDAO questionDAO) {
		this.questionDAO = questionDAO;
	}

	public void setQuestionIncubatorDAO(QuestionIncubatorDAO questionIncubatorDAO) {
		this.questionIncubatorDAO = questionIncubatorDAO;
	}

	public void setImageHolderDAO(ImageHolderDAO imageHolderDAO) {
		this.imageHolderDAO = imageHolderDAO;
	}

	public void setImageDAO(ImageDAO imageDAO) {
		this.imageDAO = imageDAO;
	}
	
}

