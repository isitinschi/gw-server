package com.guesswhat.server.persistence.jpa.dao.impl;

import static com.guesswhat.server.persistence.jpa.cfg.OfyService.ofy;

import java.util.ArrayList;
import java.util.List;

import com.guesswhat.server.persistence.jpa.dao.RecordDAO;
import com.guesswhat.server.persistence.jpa.entity.Record;

public class RecordDAOImpl extends RecordDAO {
	
	@Override
	public Class<Record> getEntityClass() {
		return Record.class;
	}
	
	
	public List<Record> findTop() {
		List<Record> results = ofy().load().type(getEntityClass()).order("-points").limit(10).list();
		
		if (results == null) {
        	results = new ArrayList<Record>();
        }
        
        return results;
	}

	@Override
	public int findRecordPlace(Record record) {
		List<Record> results = ofy().load().type(getEntityClass()).order("-points").project("points").distinct(true).list();

        int place = 1;
        for (Record result : results) {
        	if (record.getPoints() == result.getPoints()) {
        		return place;
        	}
        	++ place;
        }
        
		return 0;
	}

	@Override
	public Record findByUserId(String userId) {
		return findFirstByFilter("userId", userId);
	}
	
}
