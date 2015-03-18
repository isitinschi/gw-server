package com.guesswhat.server.persistence.jpa.dao;

import java.util.List;

import com.guesswhat.server.persistence.jpa.entity.Record;

public interface RecordDAO extends EntityDAO<Record> {
	List<Record> findTop();
}
