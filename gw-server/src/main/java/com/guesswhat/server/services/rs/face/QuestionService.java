package com.guesswhat.server.services.rs.face;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.guesswhat.server.services.rs.dto.QuestionDTO;

public interface QuestionService {
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/find/{userId}")
	String findQuestionForUser(@PathParam("userId") int userId);
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	void addQuestion(QuestionDTO question);
}
