package com.guesswhat.server.persistence.jpa.dao.impl;

import com.guesswhat.server.persistence.jpa.dao.EntityDAO;
import com.guesswhat.server.persistence.jpa.entity.QuestionIncubator;

public class QuestionIncubatorDAOImpl extends EntityDAO<QuestionIncubator> {
	
	@Override
	public Class<QuestionIncubator> getEntityClass() {
		return QuestionIncubator.class;
	}	
	
}
