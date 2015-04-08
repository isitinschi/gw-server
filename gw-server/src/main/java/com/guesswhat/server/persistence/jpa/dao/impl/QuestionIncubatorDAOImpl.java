package com.guesswhat.server.persistence.jpa.dao.impl;

import javax.jdo.PersistenceManager;

import com.google.appengine.api.datastore.Key;
import com.guesswhat.server.persistence.jpa.dao.EntityDAO;
import com.guesswhat.server.persistence.jpa.entity.QuestionIncubator;

public class QuestionIncubatorDAOImpl extends EntityDAO<QuestionIncubator> {

	@Override
	public void update(QuestionIncubator questionIncubator) {
		PersistenceManager pm = getPersistenceManagerFactory()
				.getPersistenceManager();
		
		Key imageQuestion = questionIncubator.getImageQuestion();
		Key imageAnswer = questionIncubator.getImageAnswer();
		String answer1 = questionIncubator.getAnswer1();
		String answer2 = questionIncubator.getAnswer2();
		String answer3 = questionIncubator.getAnswer3();
		String answer4 = questionIncubator.getAnswer4();
		String correctAnswer = questionIncubator.getCorrectAnswer();

		try {
			pm.currentTransaction().begin();
			questionIncubator = pm.getObjectById(QuestionIncubator.class, questionIncubator.getKey());
			questionIncubator.setAnswer1(answer1);
			questionIncubator.setAnswer2(answer2);
			questionIncubator.setAnswer3(answer3);
			questionIncubator.setAnswer4(answer4);
			questionIncubator.setCorrectAnswer(correctAnswer);
			questionIncubator.setImageQuestion(imageQuestion);
			questionIncubator.setImageAnswer(imageAnswer);
			pm.makePersistent(questionIncubator);
			pm.currentTransaction().commit();
		} catch (Exception ex) {
			pm.currentTransaction().rollback();
			throw new RuntimeException(ex);
		} finally {
			pm.close();
		}
	}
	
	@Override
	public Class<QuestionIncubator> getEntityClass() {
		return QuestionIncubator.class;
	}
	
	
}
