package com.guesswhat.server.persistence.jpa.dao;

import com.guesswhat.server.persistence.jpa.entity.QuestionIncubator;

public abstract class QuestionIncubatorDAO extends EntityDAO<QuestionIncubator> {
	
	@Override
	public Class<QuestionIncubator> getEntityClass() {
		return QuestionIncubator.class;
	}
}
