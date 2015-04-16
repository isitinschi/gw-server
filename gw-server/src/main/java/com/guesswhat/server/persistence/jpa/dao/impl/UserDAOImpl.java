package com.guesswhat.server.persistence.jpa.dao.impl;

import com.guesswhat.server.persistence.jpa.dao.UserDAO;
import com.guesswhat.server.persistence.jpa.entity.User;
import com.guesswhat.server.services.security.cfg.UserRole;

public class UserDAOImpl extends UserDAO {

	@Override
	public void update(User t) {
		// Nothing to do		
	}

	@Override
	public User findAdmin() {
		return findByFilter("role == '" + UserRole.ADMIN.toString() + "'");
	}
	
	@Override
	public User findUser(String username) {
		return findByFilter("username == '" + username + "'");
	}
	
}
