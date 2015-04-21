package com.guesswhat.server.persistence.jpa.dao.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.guesswhat.server.persistence.jpa.dao.RecordDAO;
import com.guesswhat.server.persistence.jpa.entity.Record;

public class RecordDAOImpl extends RecordDAO {


	@Override
	public void update(Record record) {
		PersistenceManager pm = getPersistenceManagerFactory()
				.getPersistenceManager();
		String userId = record.getUserId();
		int points = record.getPoints();

		try {
			pm.currentTransaction().begin();
			record = pm.getObjectById(Record.class, record.getKey());
			record.setUserId(userId);
			record.setPoints(points);
			// pm.makePersistent(record);			
		} finally {
			pm.currentTransaction().commit();
			pm.close();
		}
	}
	
	@Override
	public Class<Record> getEntityClass() {
		return Record.class;
	}
	
	
	public List<Record> findTop() {
		PersistenceManager pm = getPersistenceManagerFactory()
				.getPersistenceManager();
		
        List<Record> results = null;
        Query q = pm.newQuery(getEntityClass());
        q.setOrdering("points descending");
        q.setRange(0, 10);
        
        try {
        	pm.currentTransaction().begin();
            results = (List<Record>) q.execute();         
        } finally {
        	pm.currentTransaction().commit();
            q.closeAll();
            pm.close();
        }        
        
        if (results == null) {
        	results = new ArrayList<Record>();
        }
        
        return results;
	}

	@Override
	public int findRecordPlace(Record record) {
		PersistenceManager pm = getPersistenceManagerFactory()
				.getPersistenceManager();
		
        List<Integer> results = null;
        Query q = pm.newQuery(getEntityClass());
        q.setGrouping("points");
        q.setResult("points");
        
        List<Integer> pointList = null;
        try {
        	pm.currentTransaction().begin();
            results = (List<Integer>) q.execute();
            
            if (results == null) {
            	return 0;
            }
            
            pointList = new ArrayList<Integer>(results);
        } finally {
        	pm.currentTransaction().commit();
            q.closeAll();
            pm.close();
        }
        
        Collections.sort(pointList, Collections.reverseOrder());
        int place = 1;
        for (Integer points : pointList) {
        	if (record.getPoints() == points) {
        		return place;
        	}
        	++ place;
        }
        
		return 0;
	}

	@Override
	public Record findByUserId(String userId) {
		return findByFilter("userId == '" + userId + "'");
	}

	
}
