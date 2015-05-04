package com.guesswhat.server.service.rs.face;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.guesswhat.server.service.rs.dto.RecordDTO;


public interface RecordService {
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	Response saveUserRecord(RecordDTO recordDTO);
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/top")
	Response findTopRecords();
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/place/{userId}")
	Response findUserPlace(@PathParam("userId") String userId);	
}
