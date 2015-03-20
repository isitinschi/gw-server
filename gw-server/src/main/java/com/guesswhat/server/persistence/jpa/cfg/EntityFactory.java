package com.guesswhat.server.persistence.jpa.cfg;

import com.guesswhat.server.persistence.jpa.dao.QuestionDAO;
import com.guesswhat.server.persistence.jpa.dao.QuestionIncubatorDAO;
import com.guesswhat.server.persistence.jpa.dao.RecordDAO;
import com.guesswhat.server.persistence.jpa.dao.impl.QuestionDAOImpl;
import com.guesswhat.server.persistence.jpa.dao.impl.QuestionIncubatorDAOImpl;
import com.guesswhat.server.persistence.jpa.dao.impl.RecordDAOImpl;


public class EntityFactory {
    
	private static EntityFactory instance = null;
	
	private QuestionDAO questionDAO = null;
	private QuestionIncubatorDAO questionIncubatorDAO = null;
	private RecordDAO recordDAO = null;
	
	private EntityFactory() {
		questionDAO = new QuestionDAOImpl();
		questionIncubatorDAO = new QuestionIncubatorDAOImpl();
		recordDAO = new RecordDAOImpl();
	}
	
	public static EntityFactory getInstance() {
		if (instance == null) {
			instance = new EntityFactory();
		}
		
		return instance;
	}

	public QuestionDAO getQuestionDAO() {
		return questionDAO;
	}

	public RecordDAO getRecordDAO() {
		return recordDAO;
	}

	public QuestionIncubatorDAO getQuestionIncubatorDAO() {
		return questionIncubatorDAO;
	}
}
