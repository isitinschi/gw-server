package com.guesswhat.server.persistence.jpa.dao;

import com.guesswhat.server.persistence.jpa.entity.Question;

public abstract class QuestionDAO extends EntityDAO<Question> {
	
	@Override
	public Class<Question> getEntityClass() {
		return Question.class;
	}
}
