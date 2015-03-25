package com.guesswhat.server.persistence.jpa.dao.impl;

import javax.jdo.PersistenceManager;

import com.guesswhat.server.persistence.jpa.dao.EntityDAO;
import com.guesswhat.server.persistence.jpa.entity.Question;

public class QuestionDAOImpl extends EntityDAO<Question> {

	@Override
	public void update(Question question) {
		PersistenceManager pm = getPersistenceManagerFactory()
				.getPersistenceManager();
		String answer1 = question.getAnswer1();
		String answer2 = question.getAnswer2();
		String answer3 = question.getAnswer3();
		String answer4 = question.getAnswer4();
		String correctAnswer = question.getCorrectAnswer();

		try {
			pm.currentTransaction().begin();
			question = pm.getObjectById(Question.class, question.getKey());
			question.setAnswer1(answer1);
			question.setAnswer2(answer2);
			question.setAnswer3(answer3);
			question.setAnswer4(answer4);
			question.setCorrectAnswer(correctAnswer);
			pm.makePersistent(question);
			pm.currentTransaction().commit();
		} catch (Exception ex) {
			pm.currentTransaction().rollback();
			throw new RuntimeException(ex);
		} finally {
			pm.close();
		}
	}

	@Override
	public Class getEntityClass() {
		return Question.class;
	}
	
}
