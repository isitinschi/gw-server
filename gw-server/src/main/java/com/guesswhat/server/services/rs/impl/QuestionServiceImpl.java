package com.guesswhat.server.services.rs.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;

import com.guesswhat.server.persistence.jpa.cfg.EntityFactory;
import com.guesswhat.server.persistence.jpa.entity.Image;
import com.guesswhat.server.persistence.jpa.entity.ImageHolder;
import com.guesswhat.server.persistence.jpa.entity.Question;
import com.guesswhat.server.persistence.jpa.entity.QuestionIncubator;
import com.guesswhat.server.services.rs.dto.QuestionDTO;
import com.guesswhat.server.services.rs.face.DatabaseService;
import com.guesswhat.server.services.rs.face.QuestionService;

@Path("/questions")
public class QuestionServiceImpl implements QuestionService {

	@Autowired
	private DatabaseService databaseService;
	
	@Override
	@RolesAllowed("READER")
	public Response findQuestions() {
		List<Question> questions = EntityFactory.getInstance().getQuestionDAO().findAll();
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
		EntityFactory.getInstance().getQuestionIncubatorDAO().save(questionIncubator);
		
		return Response.ok(questionIncubator.getId()).build();
	}

	@Override
	@RolesAllowed("READER")
	public Response findQuestion(Long questionId) {
		Question question = EntityFactory.getInstance().getQuestionDAO().find(questionId);
		QuestionDTO questionDTO = new QuestionDTO(question);
		
		return Response.ok(questionDTO).build();
	}

	@Override
	@RolesAllowed("WRITER")
	public Response deleteQuestion(Long questionId) {
		Question question = EntityFactory.getInstance().getQuestionDAO().find(questionId);
		if (question != null) {
			Long imageHolderId = question.getImageQuestionId();
			removeImageHolder(imageHolderId);
			imageHolderId = question.getImageAnswerId();
			removeImageHolder(imageHolderId);
			
			EntityFactory.getInstance().getQuestionDAO().remove(questionId);
			databaseService.incrementVersion();
		}
		
		return Response.ok().build();
	}

	private void removeImageHolder(Long imageHolderId) {
		if (imageHolderId != null) {
			ImageHolder imageHolder = EntityFactory.getInstance().getImageHolderDAO().find(imageHolderId);
			if (imageHolder != null) {
				removeImage(imageHolder.getLdpiImageId());
				removeImage(imageHolder.getMdpiImageId());
				removeImage(imageHolder.getHdpiImageId());
				removeImage(imageHolder.getXhdpiImageId());
				removeImage(imageHolder.getXxhdpiImageId());
				
				EntityFactory.getInstance().getImageHolderDAO().remove(imageHolderId);
			}
		}
	}

	private void removeImage(Long imageId) {
		if (imageId != null) {
			Image image = EntityFactory.getInstance().getImageDAO().find(imageId);
			if (image != null) {
				Image secondPart = null;
				if (image.getSecondPart() != null) {
					secondPart = EntityFactory.getInstance().getImageDAO().find(image.getSecondPart());
				}
				
				EntityFactory.getInstance().getImageDAO().remove(imageId);
				if (secondPart != null) {
					EntityFactory.getInstance().getImageDAO().remove(secondPart.getId());
				}
			}
		}
	}

	@Override
	@RolesAllowed("WRITER")
	public Response updateQuestion(QuestionDTO questionDTO) {
		Question question = new Question(questionDTO);
		EntityFactory.getInstance().getQuestionDAO().update(question);
		
		return Response.ok().build();
	}

	@Override
	@RolesAllowed("WRITER")
	public Response buildQuestion(Long questionId) {
		QuestionIncubator questionIncubator = EntityFactory.getInstance().getQuestionIncubatorDAO().find(questionId);
		
		ImageHolder imageHolderQuestion = null;
		if (questionIncubator.getImageQuestionId() != null) {
			imageHolderQuestion = EntityFactory.getInstance().getImageHolderDAO().find(questionIncubator.getImageQuestionId());
		
			ImageHolder imageHolderAnswer = null;
			if (questionIncubator.getImageAnswerId() != null) {
				imageHolderAnswer = EntityFactory.getInstance().getImageHolderDAO().find(questionIncubator.getImageAnswerId());
			}
			
			if (imageHolderQuestion.isFull()) {
				Question question = new Question(questionIncubator);
				if (imageHolderAnswer == null || !imageHolderAnswer.isFull()) {
					question.setImageAnswerId(null);
				}
				EntityFactory.getInstance().getQuestionDAO().save(question);
				EntityFactory.getInstance().getQuestionIncubatorDAO().remove(questionIncubator.getId());
				databaseService.incrementVersion();
			}
		}
		
		return Response.ok().build();
	}
	
}

