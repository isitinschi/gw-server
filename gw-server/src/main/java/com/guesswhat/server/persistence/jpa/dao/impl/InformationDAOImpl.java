package com.guesswhat.server.persistence.jpa.dao.impl;

import static com.guesswhat.server.persistence.jpa.cfg.OfyService.ofy;

import com.guesswhat.server.persistence.jpa.dao.InformationDAO;
import com.guesswhat.server.persistence.jpa.entity.Information;

public class InformationDAOImpl extends InformationDAO {

	@Override
	public void increment() {
		Information information = ofy().load().type(getEntityClass()).first().now();
		
		if (information == null) {
        	information = new Information(1);
        	save(information);
        } else {
			information.setDatabaseVersion(information.getDatabaseVersion() + 1);
			update(information);
        }
	}
	
	@Override
	public Information getInformation() {
		return ofy().load().type(getEntityClass()).first().now();
	}
	
}
