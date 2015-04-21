package com.guesswhat.server.services.rs.face;

import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

public interface SecurityService {

	@POST
	@Path("/create")
	Response createUser(@FormParam("username") String username, @FormParam("password") String password, @FormParam("role") String role);
	
	@POST
	@Path("/create/admin")
	Response createAdmin(@FormParam("username") String username, @FormParam("password") String password);
	
	@DELETE
	@Path("/delete/{username}")
	Response deleteUser(@PathParam("username") String username);
}