package com.guesswhat.server.persistence.jpa.dao.impl;

import com.guesswhat.server.persistence.jpa.dao.UserDAO;
import com.guesswhat.server.persistence.jpa.entity.User;
import com.guesswhat.server.services.security.cfg.UserRole;

public class UserDAOImpl extends UserDAO {

	@Override
	public User findAdmin() {
		return findFirstByFilter("role", UserRole.ADMIN);
	}
	
	@Override
	public User findUser(String username) {
		return findFirstByFilter("username", username);
	}
	
}
