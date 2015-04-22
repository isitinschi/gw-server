package com.guesswhat.server.persistence.jpa.dao.impl;

import com.guesswhat.server.persistence.jpa.dao.EntityDAO;
import com.guesswhat.server.persistence.jpa.entity.Question;

public class QuestionDAOImpl extends EntityDAO<Question> {

	@Override
	public Class<Question> getEntityClass() {
		return Question.class;
	}
	
}
