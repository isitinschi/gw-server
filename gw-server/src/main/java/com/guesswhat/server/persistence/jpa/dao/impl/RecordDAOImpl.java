package com.guesswhat.server.persistence.jpa.dao.impl;

import java.util.List;

import javax.jdo.PersistenceManager;

import com.guesswhat.server.persistence.jpa.jdo.EntityJDO;
import com.guesswhat.server.persistence.jpa.dao.RecordDAO;
import com.guesswhat.server.persistence.jpa.entity.Record;

public class RecordDAOImpl extends EntityJDO implements RecordDAO {

	@Override
	public void save(Record record) {
		PersistenceManager pm = getPersistenceManagerFactory()
				.getPersistenceManager();
		try {
			pm.makePersistent(record);
		} finally {
			pm.close();
		}
	}

	@Override
	public void update(Record record) {
		PersistenceManager pm = getPersistenceManagerFactory()
				.getPersistenceManager();
		int userId = record.getUserId();
		String userName = record.getUserName();
		int recordPoints = record.getRecordPoints();

		try {
			pm.currentTransaction().begin();
			record = pm.getObjectById(Record.class, record.getId());
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
	public void remove(Record record) {
		PersistenceManager pm = getPersistenceManagerFactory()
				.getPersistenceManager();
		try {
			pm.currentTransaction().begin();
			
			record = pm.getObjectById(Record.class, record.getId());
			pm.deletePersistent(record);

			pm.currentTransaction().commit();
		} catch (Exception ex) {
			pm.currentTransaction().rollback();
			throw new RuntimeException(ex);
		} finally {
			pm.close();
		}
	}

	@Override
	public Record find(Record record) {
		PersistenceManager pm = getPersistenceManagerFactory()
				.getPersistenceManager();
		try {
			record = pm.getObjectById(Record.class, record.getId());
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		} finally {
			pm.close();
		}
		
		return record;
	}

	@Override
	public List<Record> findTop() {
		// TODO Auto-generated method stub
		return null;
	}

	
}
