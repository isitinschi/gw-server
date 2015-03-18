package com.guesswhat.server.services.rs.face;

import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

public interface RecordService {
	
	@PUT
	void saveUserRecord(@QueryParam("userId") int userId, @QueryParam("recordPoints") int recordPoints);
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	String findTopRecords();
}
