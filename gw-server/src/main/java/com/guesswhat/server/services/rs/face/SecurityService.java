package com.guesswhat.server.services.rs.face;

import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

public interface SecurityService {

	@POST
	@Path("/create")
	void createUser(@FormParam("username") String username, @FormParam("password") String password, @FormParam("role") String role);
	
	@POST
	@Path("/create/admin")
	void createAdmin(@FormParam("username") String username, @FormParam("password") String password);
	
	@DELETE
	@Path("/delete/{username}")
	void deleteUser(@PathParam("username") String username);
}
