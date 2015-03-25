package com.guesswhat.server.services.rs.face;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.guesswhat.server.persistence.jpa.entity.Question;
import com.guesswhat.server.services.rs.dto.QuestionDTO;

public interface QuestionService {

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/create")
	String createQuestion(QuestionDTO questionDTO);
	
	@DELETE
	@Path("/delete/{questionId}")
	void deleteQuestion(@PathParam("questionId") Long questionId);
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/update")
	void updateQuestion(QuestionDTO questionDTO);

	@POST
	@Path("find/{questionId}")
	@Produces(MediaType.APPLICATION_JSON)
	QuestionDTO findQuestion(@PathParam("questionId") Long questionId);
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/findall")
	List<QuestionDTO> findQuestions();
}
