package com.guesswhat.server.persistence.jpa.dao;

import java.util.List;

import com.guesswhat.server.persistence.jpa.dao.EntityDAO;
import com.guesswhat.server.persistence.jpa.entity.Record;

public abstract class RecordDAO extends EntityDAO<Record> {
	
	public abstract List<Record> findTop();
	public abstract int findRecordPlace(Record record);
	public abstract Record findByUserId(String userId);
	
	@Override
	public Class<Record> getEntityClass() {
		return Record.class;
	}
	
}
