package com.guesswhat.server.services.rs.impl;

import java.util.List;

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
		List<Information> informations = EntityFactory.getInstance().getInformationDAO().findAll();
		int databaseVersion = 0;
		if (informations != null && !informations.isEmpty()) {
			databaseVersion = informations.get(0).getDatabaseVersion();
		} else {
			databaseVersion = 1;
			Information information = new Information();
			information.setDatabaseVersion(databaseVersion);
			EntityFactory.getInstance().getInformationDAO().save(information);
		}
		
		return Response.ok(databaseVersion).build();
	}

	public static void incrementVersion() {
		EntityFactory.getInstance().getInformationDAO().increment();
	}
	
}

