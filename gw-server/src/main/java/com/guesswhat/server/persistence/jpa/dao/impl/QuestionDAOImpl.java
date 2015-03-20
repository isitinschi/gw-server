package com.guesswhat.server.persistence.jpa.dao.impl;

import javax.jdo.PersistenceManager;

import com.guesswhat.server.persistence.jpa.jdo.EntityJDO;
import com.guesswhat.server.persistence.jpa.dao.QuestionDAO;
import com.guesswhat.server.persistence.jpa.entity.Question;

public class QuestionDAOImpl extends EntityJDO implements QuestionDAO {

	@Override
	public void save(Question question) {
		PersistenceManager pm = getPersistenceManagerFactory()
				.getPersistenceManager();
		try {
			pm.makePersistent(question);
		} finally {
			pm.close();
		}
	}

	@Override
	public void update(Question question) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void remove(Question question) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Question find(Question question) {
		return find(question.getId());
	}

	@Override
	public Question find(Long id) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
