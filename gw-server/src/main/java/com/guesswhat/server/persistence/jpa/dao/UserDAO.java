package com.guesswhat.server.persistence.jpa.dao;

import com.guesswhat.server.persistence.jpa.dao.EntityDAO;
import com.guesswhat.server.persistence.jpa.entity.User;

public abstract class UserDAO extends EntityDAO<User> {
	
	public abstract User findAdmin();
	public abstract User findUser(String username);
	
	@Override
	public Class<User> getEntityClass() {
		return User.class;
	}
	
}
