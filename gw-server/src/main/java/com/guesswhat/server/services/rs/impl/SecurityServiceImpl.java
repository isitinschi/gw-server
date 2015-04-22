package com.guesswhat.server.services.rs.impl;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;

import com.guesswhat.server.persistence.jpa.dao.UserDAO;
import com.guesswhat.server.persistence.jpa.entity.User;
import com.guesswhat.server.services.rs.face.SecurityService;
import com.guesswhat.server.services.security.cfg.UserRole;

@Path("/security")
public class SecurityServiceImpl implements SecurityService {

	@Autowired private UserDAO userDAO;
	
	@Override
	@RolesAllowed("ADMIN")
	public Response createUser(String username, String password, String role) {
		if (username != null && !username.equals("") && password != null && !password.equals("") &&
				username.length() > 10 && password.length() > 10) {
			if (!role.equals(UserRole.ADMIN.toString())) {
				User user = userDAO.findUser(username);
				if (user == null) {
					User newUser = new User(username, password, role);
					userDAO.save(newUser);
				}
			}
		}
		
		return Response.ok().build();
	}

	@Override
	@PermitAll
	public Response createAdmin(String username, String password) {
		if (username != null && !username.equals("") && password != null && !password.equals("") &&
				username.length() > 10 && password.length() > 10) {
			User admin = userDAO.findAdmin();
			if (admin == null) {
				admin = new User(username, password, UserRole.ADMIN.toString());
				userDAO.save(admin);
			}
		}
		
		return Response.ok().build();
	}

	@Override
	@RolesAllowed("ADMIN")
	public Response deleteUser(String username) {
		User user = userDAO.findUser(username);
		if (user != null && !user.getRole().equals(UserRole.ADMIN.toString())) {
			userDAO.remove(user.getId());
		}
		
		return Response.ok().build();
	}

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}
	
}
