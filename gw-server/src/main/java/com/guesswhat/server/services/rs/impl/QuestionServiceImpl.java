package com.guesswhat.server.services.rs.impl;

import javax.ws.rs.Path;

import com.guesswhat.server.persistence.jpa.cfg.EntityFactory;
import com.guesswhat.server.persistence.jpa.entity.Question;
import com.guesswhat.server.services.rs.dto.QuestionDTO;
import com.guesswhat.server.services.rs.face.QuestionService;

@Path("/questions")
public class QuestionServiceImpl implements QuestionService {

	@Override
	public String findQuestion() {
		return "Hi!-)";
	}

	@Override
	public void addQuestion(QuestionDTO questionDTO) {
		Question question = new Question(questionDTO);
		EntityFactory.getInstance().getQuestionDAO().save(question);
	}

}

