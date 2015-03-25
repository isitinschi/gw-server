package com.guesswhat.server.services.rs.face;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import com.sun.jersey.multipart.MultiPart;

public interface ImageService {

	@PUT
	@Path("create/question/{questionId}/{imageType}")
	@Consumes("multipart/mixed")
	void createQuestionImage(@PathParam("questionId") Long questionId, @PathParam("imageType") String imageType, MultiPart multiPart);
	
	@PUT
	@Path("create/answer/{questionId}/{imageType}")
	@Consumes("multipart/mixed")
	void createAnswerImage(@PathParam("questionId") Long questionId, @PathParam("imageType") String imageType, MultiPart multiPart);
	
	@POST
	@Path("find/question/{questionId}/{imageType}")
	@Produces("multipart/mixed")
	String findQuestionImage(@PathParam("questionId") Long questionId, @PathParam("imageType") String imageType);
	
	@POST
	@Path("find/answer/{questionId}/{imageType}")
	@Produces("multipart/mixed")
	String findAnswerImage(@PathParam("questionId") Long questionId, @PathParam("imageType") String imageType);
}
