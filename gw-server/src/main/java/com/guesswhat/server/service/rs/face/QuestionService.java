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

import com.guesswhat.server.service.rs.dto.ComposedQuestionDTO;
import com.guesswhat.server.service.rs.dto.QuestionDTO;

public interface QuestionService {

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/create")
	Response createQuestion(ComposedQuestionDTO questionDTO);
	
	@DELETE
	@Path("/delete/{questionId}")
	Response deleteQuestion(@PathParam("questionId") Long questionId);
	
	@DELETE
	@Path("/delete")
	Response deleteQuestions();
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/update")
	Response updateQuestion(QuestionDTO questionDTO);

	@POST
	@Path("/find/{questionId}")
	@Produces(MediaType.APPLICATION_JSON)
	Response findQuestion(@PathParam("questionId") Long questionId);
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/findall")
	Response findQuestions();
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/download/{questionId}")
	Response downloadQuestion(@PathParam("questionId") Long questionId);
	
}
