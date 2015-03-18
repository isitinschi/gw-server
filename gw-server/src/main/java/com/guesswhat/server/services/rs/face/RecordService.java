package com.guesswhat.server.services.rs.face;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.guesswhat.server.services.rs.dto.RecordDTO;


public interface RecordService {
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	void saveUserRecord(RecordDTO recordDTO);
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	String findTopRecords();
}
