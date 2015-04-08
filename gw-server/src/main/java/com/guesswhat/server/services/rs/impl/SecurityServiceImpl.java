package com.guesswhat.server.services.rs.impl;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Path;

import com.guesswhat.server.persistence.jpa.cfg.EntityFactory;
import com.guesswhat.server.persistence.jpa.entity.User;
import com.guesswhat.server.persistence.jpa.entity.UserRole;
import com.guesswhat.server.services.rs.face.SecurityService;

@Path("/security")
public class SecurityServiceImpl implements SecurityService {

	@Override
	@RolesAllowed("ADMIN")
	public void createUser(String username, String password, String role) {
		if (username != null && !username.equals("") && password != null && !password.equals("") &&
				username.length() > 10 && password.length() > 10) {
			if (!role.equals(UserRole.ADMIN.toString())) {
				User user = new User(username, password, role);
				EntityFactory.getInstance().getUserDAO().save(user);
			}
		}
	}

	@Override
	@PermitAll
	public void createAdmin(String username, String password) {
		if (username != null && !username.equals("") && password != null && !password.equals("") &&
				username.length() > 10 && password.length() > 10) {
			User admin = EntityFactory.getInstance().getUserDAO().findAdmin();
			if (admin == null) {
				admin = new User(username, password, UserRole.ADMIN.toString());
				EntityFactory.getInstance().getUserDAO().save(admin);
			}
		}
	}

	@Override
	@RolesAllowed("ADMIN")
	public void deleteUser(String username) {
		User user = EntityFactory.getInstance().getUserDAO().findUser(username);
		if (user != null && !user.getRole().equals(UserRole.ADMIN.toString())) {
			EntityFactory.getInstance().getUserDAO().remove(user.getKey());
		}
	}
}
