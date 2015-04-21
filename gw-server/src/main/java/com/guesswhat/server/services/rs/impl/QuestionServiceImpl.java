package com.guesswhat.server.services.rs.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import com.google.appengine.api.datastore.Key;
import com.guesswhat.server.persistence.jpa.cfg.EntityFactory;
import com.guesswhat.server.persistence.jpa.entity.Image;
import com.guesswhat.server.persistence.jpa.entity.ImageHolder;
import com.guesswhat.server.persistence.jpa.entity.Question;
import com.guesswhat.server.persistence.jpa.entity.QuestionIncubator;
import com.guesswhat.server.services.rs.dto.QuestionDTO;
import com.guesswhat.server.services.rs.face.QuestionService;

@Path("/questions")
public class QuestionServiceImpl implements QuestionService {

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
		
		return Response.ok(questionIncubator.getKey().getId()).build();
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
			Key imageHolderKey = question.getImageQuestion();
			removeImageHolder(imageHolderKey);
			imageHolderKey = question.getImageAnswer();
			removeImageHolder(imageHolderKey);
			
			EntityFactory.getInstance().getQuestionDAO().remove(questionId);
			DatabaseServiceImpl.incrementVersion();
		}
		
		return Response.ok().build();
	}

	private void removeImageHolder(Key imageHolderKey) {
		if (imageHolderKey != null) {
			ImageHolder imageHolder = EntityFactory.getInstance().getImageHolderDAO().find(imageHolderKey);
			if (imageHolder != null) {
				removeImage(imageHolder.getLdpiImage());
				removeImage(imageHolder.getMdpiImage());
				removeImage(imageHolder.getHdpiImage());
				removeImage(imageHolder.getXhdpiImage());
				removeImage(imageHolder.getXxhdpiImage());
				
				EntityFactory.getInstance().getImageHolderDAO().remove(imageHolderKey);
			}
		}
	}

	private void removeImage(Key imageKey) {
		if (imageKey != null) {
			Image image = EntityFactory.getInstance().getImageDAO().find(imageKey);
			if (image != null) {
				Image secondPart = null;
				if (image.getSecondPart() != null) {
					secondPart = EntityFactory.getInstance().getImageDAO().find(image.getSecondPart());
				}
				
				EntityFactory.getInstance().getImageDAO().remove(imageKey);
				if (secondPart != null) {
					EntityFactory.getInstance().getImageDAO().remove(secondPart.getKey());
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
		if (questionIncubator.getImageQuestion() != null) {
			imageHolderQuestion = EntityFactory.getInstance().getImageHolderDAO().find(questionIncubator.getImageQuestion());
		
			ImageHolder imageHolderAnswer = null;
			if (questionIncubator.getImageAnswer() != null) {
				imageHolderAnswer = EntityFactory.getInstance().getImageHolderDAO().find(questionIncubator.getImageAnswer());
			}
			
			if (imageHolderQuestion.isFull()) {
				Question question = new Question(questionIncubator);
				if (imageHolderAnswer == null || !imageHolderAnswer.isFull()) {
					question.setImageAnswer(null);
				}
				EntityFactory.getInstance().getQuestionDAO().save(question);
				EntityFactory.getInstance().getQuestionIncubatorDAO().remove(questionIncubator.getKey());
				DatabaseServiceImpl.incrementVersion();
			}
		}
		
		return Response.ok().build();
	}
	
}

