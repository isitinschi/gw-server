package com.guesswhat.server.services.rs.face;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.guesswhat.server.services.rs.dto.QuestionDTO;
import com.sun.jersey.multipart.MultiPart;

public interface QuestionService {

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/find")
	String findQuestion();

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	String addQuestion(QuestionDTO question);

	@POST
	@Path("/{questionId}/{imageName}")
	@Consumes("multipart/mixed")
	void addQuestionImage(@PathParam("questionId") Long questionId, @PathParam("imageName") String imageName, MultiPart multiPart);
}
