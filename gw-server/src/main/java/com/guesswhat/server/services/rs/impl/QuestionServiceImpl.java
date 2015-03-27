package com.guesswhat.server.services.rs.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Path;
import javax.ws.rs.core.GenericEntity;

import com.guesswhat.server.persistence.jpa.cfg.EntityFactory;
import com.guesswhat.server.persistence.jpa.entity.Question;
import com.guesswhat.server.persistence.jpa.entity.QuestionIncubator;
import com.guesswhat.server.services.rs.dto.QuestionDTO;
import com.guesswhat.server.services.rs.face.QuestionService;

@Path("/questions")
public class QuestionServiceImpl implements QuestionService {

	@Override
	public GenericEntity<List<QuestionDTO>> findQuestions() {
		List<Question> questions = EntityFactory.getInstance().getQuestionDAO().findAll();
		List<QuestionDTO> questionsDTO = new ArrayList<QuestionDTO>();
		for (Question question : questions) {
			QuestionDTO questionDTO = new QuestionDTO(question);
			questionsDTO.add(questionDTO);
		}
		
		return new GenericEntity<List<QuestionDTO>>(questionsDTO) {};
	}

	@Override
	public String createQuestion(QuestionDTO questionDTO) {
		QuestionIncubator questionIncubator = new QuestionIncubator(questionDTO);
		EntityFactory.getInstance().getQuestionIncubatorDAO().save(questionIncubator);
		return String.valueOf(questionIncubator.getKey().getId());
	}

	@Override
	public QuestionDTO findQuestion(Long questionId) {
		Question question = EntityFactory.getInstance().getQuestionDAO().find(questionId);
		QuestionDTO questionDTO = new QuestionDTO(question);
		return questionDTO;
	}

	@Override
	public void deleteQuestion(Long questionId) {
		EntityFactory.getInstance().getQuestionDAO().remove(questionId);
	}

	@Override
	public void updateQuestion(QuestionDTO questionDTO) {
		Question question = new Question(questionDTO);
		EntityFactory.getInstance().getQuestionDAO().update(question);
	}
}

