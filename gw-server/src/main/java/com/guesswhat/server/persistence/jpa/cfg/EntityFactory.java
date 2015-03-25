package com.guesswhat.server.persistence.jpa.cfg;

import com.guesswhat.server.persistence.jpa.dao.EntityDAO;
import com.guesswhat.server.persistence.jpa.dao.impl.ImageDAOImpl;
import com.guesswhat.server.persistence.jpa.dao.impl.ImageHolderDAOImpl;
import com.guesswhat.server.persistence.jpa.dao.impl.QuestionDAOImpl;
import com.guesswhat.server.persistence.jpa.dao.impl.QuestionIncubatorDAOImpl;
import com.guesswhat.server.persistence.jpa.dao.impl.RecordDAOImpl;
import com.guesswhat.server.persistence.jpa.entity.Image;
import com.guesswhat.server.persistence.jpa.entity.ImageHolder;
import com.guesswhat.server.persistence.jpa.entity.Question;
import com.guesswhat.server.persistence.jpa.entity.QuestionIncubator;
import com.guesswhat.server.persistence.jpa.entity.Record;


public class EntityFactory {
    
	private static EntityFactory instance = null;
	
	private EntityDAO<Question> questionDAO = null;
	private EntityDAO<QuestionIncubator> questionIncubatorDAO = null;
	private EntityDAO<Record> recordDAO = null;
	private EntityDAO<Image> imageDAO = null;
	private EntityDAO<ImageHolder> imageHolderDAO = null;
	
	private EntityFactory() {
		questionDAO = new QuestionDAOImpl();
		questionIncubatorDAO = new QuestionIncubatorDAOImpl();
		recordDAO = new RecordDAOImpl();
		imageDAO = new ImageDAOImpl();
		imageHolderDAO = new ImageHolderDAOImpl();
	}
	
	public static EntityFactory getInstance() {
		if (instance == null) {
			instance = new EntityFactory();
		}
		
		return instance;
	}

	public EntityDAO<Question> getQuestionDAO() {
		return questionDAO;
	}

	public EntityDAO<Record> getRecordDAO() {
		return recordDAO;
	}

	public EntityDAO<QuestionIncubator> getQuestionIncubatorDAO() {
		return questionIncubatorDAO;
	}

	public EntityDAO<Image> getImageDAO() {
		return imageDAO;
	}

	public EntityDAO<ImageHolder> getImageHolderDAO() {
		return imageHolderDAO;
	}
}
