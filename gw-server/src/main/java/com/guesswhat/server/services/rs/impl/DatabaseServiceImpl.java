package com.guesswhat.server.services.rs.impl;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;

import com.guesswhat.server.persistence.jpa.dao.ImageDAO;
import com.guesswhat.server.persistence.jpa.dao.ImageHolderDAO;
import com.guesswhat.server.persistence.jpa.dao.InformationDAO;
import com.guesswhat.server.persistence.jpa.dao.QuestionDAO;
import com.guesswhat.server.persistence.jpa.dao.RecordDAO;
import com.guesswhat.server.persistence.jpa.dao.UserDAO;
import com.guesswhat.server.persistence.jpa.entity.Information;
import com.guesswhat.server.services.rs.face.DatabaseService;

@Path("/database")
public class DatabaseServiceImpl implements DatabaseService {
	
	@Autowired private QuestionDAO questionDAO;
	@Autowired private ImageHolderDAO imageHolderDAO;
	@Autowired private ImageDAO imageDAO;
	@Autowired private UserDAO userDAO;
	@Autowired private RecordDAO recordDAO;
	@Autowired private InformationDAO informationDAO;
	
	@Override
	@RolesAllowed("READER")
	public Response getVersion() {
		Information information = informationDAO.getInformation();
		int databaseVersion = 0;
		if (information != null) {
			databaseVersion = information.getDatabaseVersion();
		} else {
			databaseVersion = 1;
			information = new Information(databaseVersion);
			informationDAO.save(information);
		}
		
		return Response.ok(databaseVersion).build();
	}
	
	@Override
	@RolesAllowed("WRITER")
	public Response dropAllData() {
		questionDAO.removeAll();
		userDAO.removeAll();		
		imageHolderDAO.removeAll();
		recordDAO.removeAll();
		imageDAO.removeAll();
		informationDAO.removeAll();
		
		return Response.ok().build();
	}

	@Override
	public void incrementVersion() {
		informationDAO.increment();
	}

	public void setQuestionDAO(QuestionDAO questionDAO) {
		this.questionDAO = questionDAO;
	}

	public void setImageHolderDAO(ImageHolderDAO imageHolderDAO) {
		this.imageHolderDAO = imageHolderDAO;
	}

	public void setImageDAO(ImageDAO imageDAO) {
		this.imageDAO = imageDAO;
	}

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	public void setRecordDAO(RecordDAO recordDAO) {
		this.recordDAO = recordDAO;
	}

	public void setInformationDAO(InformationDAO informationDAO) {
		this.informationDAO = informationDAO;
	}
	
}

