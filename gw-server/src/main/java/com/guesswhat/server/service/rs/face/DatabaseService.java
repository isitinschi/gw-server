package com.guesswhat.server.service.rs.face;

import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public interface DatabaseService {
	
	@POST
	@Path("/version")
	@Produces(MediaType.APPLICATION_JSON)
	Response getVersion();
	
	@DELETE
	@Path("/drop")
	@Produces(MediaType.APPLICATION_JSON)
	Response dropAllData();
	
	void incrementVersion();
}
