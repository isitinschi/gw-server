package com.guesswhat.server.services.rs.impl;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import com.guesswhat.server.persistence.jpa.cfg.EntityFactory;
import com.guesswhat.server.persistence.jpa.entity.User;
import com.guesswhat.server.services.rs.face.SecurityService;
import com.guesswhat.server.services.security.cfg.UserRole;

@Path("/security")
public class SecurityServiceImpl implements SecurityService {

	@Override
	@RolesAllowed("ADMIN")
	public Response createUser(String username, String password, String role) {
		if (username != null && !username.equals("") && password != null && !password.equals("") &&
				username.length() > 10 && password.length() > 10) {
			if (!role.equals(UserRole.ADMIN.toString())) {
				User user = EntityFactory.getInstance().getUserDAO().findUser(username);
				if (user == null) {
					User newUser = new User(username, password, role);
					EntityFactory.getInstance().getUserDAO().save(newUser);
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
			User admin = EntityFactory.getInstance().getUserDAO().findAdmin();
			if (admin == null) {
				admin = new User(username, password, UserRole.ADMIN.toString());
				EntityFactory.getInstance().getUserDAO().save(admin);
			}
		}
		
		return Response.ok().build();
	}

	@Override
	@RolesAllowed("ADMIN")
	public Response deleteUser(String username) {
		User user = EntityFactory.getInstance().getUserDAO().findUser(username);
		if (user != null && !user.getRole().equals(UserRole.ADMIN.toString())) {
			EntityFactory.getInstance().getUserDAO().remove(user.getKey());
		}
		
		return Response.ok().build();
	}
}
