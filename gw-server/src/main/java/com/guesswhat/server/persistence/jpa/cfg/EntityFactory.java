package com.guesswhat.server.persistence.jpa.cfg;

import com.guesswhat.server.persistence.jpa.dao.EntityDAO;
import com.guesswhat.server.persistence.jpa.dao.InformationDAO;
import com.guesswhat.server.persistence.jpa.dao.RecordDAO;
import com.guesswhat.server.persistence.jpa.dao.UserDAO;
import com.guesswhat.server.persistence.jpa.dao.impl.ImageDAOImpl;
import com.guesswhat.server.persistence.jpa.dao.impl.ImageHolderDAOImpl;
import com.guesswhat.server.persistence.jpa.dao.impl.InformationDAOImpl;
import com.guesswhat.server.persistence.jpa.dao.impl.QuestionDAOImpl;
import com.guesswhat.server.persistence.jpa.dao.impl.QuestionIncubatorDAOImpl;
import com.guesswhat.server.persistence.jpa.dao.impl.RecordDAOImpl;
import com.guesswhat.server.persistence.jpa.dao.impl.UserDAOImpl;
import com.guesswhat.server.persistence.jpa.entity.Image;
import com.guesswhat.server.persistence.jpa.entity.ImageHolder;
import com.guesswhat.server.persistence.jpa.entity.Information;
import com.guesswhat.server.persistence.jpa.entity.Question;
import com.guesswhat.server.persistence.jpa.entity.QuestionIncubator;
import com.guesswhat.server.persistence.jpa.entity.Record;


public class EntityFactory {
    
	private static EntityFactory instance = null;
	
	private EntityDAO<Question> questionDAO = null;
	private UserDAO userDAO = null;
	private EntityDAO<QuestionIncubator> questionIncubatorDAO = null;
	private RecordDAO recordDAO = null;
	private EntityDAO<Image> imageDAO = null;
	private EntityDAO<ImageHolder> imageHolderDAO = null;
	private InformationDAO informationDAO = null;
	
	private EntityFactory() {
		questionDAO = new QuestionDAOImpl();
		userDAO = new UserDAOImpl();
		questionIncubatorDAO = new QuestionIncubatorDAOImpl();
		recordDAO = new RecordDAOImpl();
		imageDAO = new ImageDAOImpl();
		imageHolderDAO = new ImageHolderDAOImpl();
		informationDAO = new InformationDAOImpl();
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

	public RecordDAO getRecordDAO() {
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

	public UserDAO getUserDAO() {
		return userDAO;
	}

	public InformationDAO getInformationDAO() {
		return informationDAO;
	}
}
