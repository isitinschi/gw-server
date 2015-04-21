package com.guesswhat.server.persistence.jpa.dao;

import com.guesswhat.server.persistence.jpa.dao.EntityDAO;
import com.guesswhat.server.persistence.jpa.entity.Information;

public abstract class InformationDAO extends EntityDAO<Information> {
	
	public abstract void increment();
	
	@Override
	public Class<Information> getEntityClass() {
		return Information.class;
	}
	
}
