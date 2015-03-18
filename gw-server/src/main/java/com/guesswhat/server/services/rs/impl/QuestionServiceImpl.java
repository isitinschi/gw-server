package com.guesswhat.server.services.rs.impl;

import javax.ws.rs.Path;

import com.guesswhat.server.services.rs.dto.QuestionDTO;
import com.guesswhat.server.services.rs.face.QuestionService;

@Path("/questions")
public class QuestionServiceImpl implements QuestionService {

	@Override
	public String findQuestionForUser(int userId) {
		return "Hi!-)";
	}

	@Override
	public void addQuestion(QuestionDTO question) {
		
	}

}

