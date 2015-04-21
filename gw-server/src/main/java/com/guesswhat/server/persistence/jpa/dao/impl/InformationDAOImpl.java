package com.guesswhat.server.persistence.jpa.dao.impl;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.guesswhat.server.persistence.jpa.dao.EntityDAO;
import com.guesswhat.server.persistence.jpa.dao.InformationDAO;
import com.guesswhat.server.persistence.jpa.entity.Information;

public class InformationDAOImpl extends InformationDAO {

	@Override
	public void increment() {
		PersistenceManager pm = getPersistenceManagerFactory()
				.getPersistenceManager();

		List<Information> results = null;
        Query q = pm.newQuery(getEntityClass());
        
        try {
        	pm.currentTransaction().begin();
            results = (List<Information>) q.execute();
            
            if (results == null || results.isEmpty()) {
            	return;
            }
            
            Information information = results.get(0);            
			int version = information.getDatabaseVersion();
			information.setDatabaseVersion(++version);
			pm.makePersistent(information);			
		} finally {
			pm.currentTransaction().commit();
            q.closeAll();
			pm.close();
		}
	}

	@Override
	public void update(Information t) {
		// Nothing to do
	}
	
}
