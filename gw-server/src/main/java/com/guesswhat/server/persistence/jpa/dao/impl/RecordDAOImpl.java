package com.guesswhat.server.persistence.jpa.dao.impl;

import java.util.List;

import javax.jdo.PersistenceManager;

import com.guesswhat.server.persistence.jpa.dao.EntityDAO;
import com.guesswhat.server.persistence.jpa.entity.Record;

public class RecordDAOImpl extends EntityDAO<Record> {


	@Override
	public void update(Record record) {
		PersistenceManager pm = getPersistenceManagerFactory()
				.getPersistenceManager();
		int userId = record.getUserId();
		String userName = record.getUserName();
		int recordPoints = record.getRecordPoints();

		try {
			pm.currentTransaction().begin();
			record = pm.getObjectById(Record.class, record.getKey());
			record.setUserId(userId);
			record.setUserName(userName);
			record.setRecordPoints(recordPoints);
			pm.makePersistent(record);
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
		return Record.class;
	}
	
	
	public List<Record> findTop() {
		// TODO Auto-generated method stub
		return null;
	}

	
}
