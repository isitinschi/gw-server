package com.guesswhat.server.services.rs.face;

import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

public interface ImageService {

	@PUT
	@Path("/create/question/{questionId}/{imageType}")
	@Consumes("application/octet-stream")
	void createQuestionImage(@PathParam("questionId") Long questionId, @PathParam("imageType") String imageType, @Context HttpServletRequest request, InputStream fileInputStream);
	
	@PUT
	@Path("/create/answer/{questionId}/{imageType}")
	@Consumes("application/octet-stream")
	void createAnswerImage(@PathParam("questionId") Long questionId, @PathParam("imageType") String imageType, @Context HttpServletRequest request, InputStream fileInputStream);
	
	@POST
	@Path("/find/question/{questionId}/{imageType}")
	@Produces("multipart/mixed")
	String findQuestionImage(@PathParam("questionId") Long questionId, @PathParam("imageType") String imageType);
	
	@POST
	@Path("/find/answer/{questionId}/{imageType}")
	@Produces("multipart/mixed")
	String findAnswerImage(@PathParam("questionId") Long questionId, @PathParam("imageType") String imageType);
}
