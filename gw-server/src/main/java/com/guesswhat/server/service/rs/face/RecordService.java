package com.guesswhat.server.service.rs.face;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.guesswhat.server.service.rs.dto.RecordDTO;
import com.guesswhat.server.service.rs.dto.RecordDTOListWrapper;

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
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/backup/download")
	Response downloadBackup();
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/backup/upload")
	Response uploadBackup(RecordDTOListWrapper recordDTOListWrapper);
	
	@DELETE
	@Path("/delete")
	Response deleteRecords();
	
}
