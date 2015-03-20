package com.guesswhat.server.persistence.jpa.dao.impl;

import javax.jdo.PersistenceManager;

import com.guesswhat.server.persistence.jpa.dao.QuestionIncubatorDAO;
import com.guesswhat.server.persistence.jpa.entity.QuestionIncubator;
import com.guesswhat.server.persistence.jpa.jdo.EntityJDO;

public class QuestionIncubatorDAOImpl extends EntityJDO implements QuestionIncubatorDAO {

	@Override
	public void save(QuestionIncubator question) {
		PersistenceManager pm = getPersistenceManagerFactory()
				.getPersistenceManager();
		try {
			pm.makePersistent(question);
		} finally {
			pm.close();
		}
	}

	@Override
	public void update(QuestionIncubator questionIncubator) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void remove(QuestionIncubator questionIncubator) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public QuestionIncubator find(QuestionIncubator questionIncubator) {
		return find(questionIncubator.getId());
	}
	
	@Override
	public QuestionIncubator find(Long questionIncubatorId) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
