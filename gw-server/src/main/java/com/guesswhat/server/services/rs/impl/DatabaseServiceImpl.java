package com.guesswhat.server.services.rs.impl;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import com.guesswhat.server.persistence.jpa.cfg.EntityFactory;
import com.guesswhat.server.persistence.jpa.entity.Information;
import com.guesswhat.server.services.rs.face.DatabaseService;

@Path("/database")
public class DatabaseServiceImpl implements DatabaseService {

	@Override
	@RolesAllowed("READER")
	public Response getVersion() {
		Information information = EntityFactory.getInstance().getInformationDAO().getInformation();
		int databaseVersion = 0;
		if (information != null) {
			databaseVersion = information.getDatabaseVersion();
		} else {
			databaseVersion = 1;
			information = new Information(databaseVersion);
			EntityFactory.getInstance().getInformationDAO().save(information);
		}
		
		return Response.ok(databaseVersion).build();
	}
	
	@Override
	@RolesAllowed("WRITER")
	public Response dropAllData() {
		EntityFactory.getInstance().getQuestionDAO().removeAll();
		EntityFactory.getInstance().getUserDAO().removeAll();		
		EntityFactory.getInstance().getImageHolderDAO().removeAll();
		EntityFactory.getInstance().getRecordDAO().removeAll();
		EntityFactory.getInstance().getImageDAO().removeAll();
		EntityFactory.getInstance().getInformationDAO().removeAll();
		
		return Response.ok().build();
	}

	@Override
	public void incrementVersion() {
		EntityFactory.getInstance().getInformationDAO().increment();
	}	
}

