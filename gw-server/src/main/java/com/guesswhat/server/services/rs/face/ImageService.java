package com.guesswhat.server.services.rs.face;

import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.guesswhat.server.persistence.jpa.entity.ImageHolder;

public interface ImageService {

	@POST
	@Path("/create/question/{questionId}/{imageType}")
	@Consumes("application/octet-stream")
	Response createQuestionImage(@PathParam("questionId") Long questionId, @PathParam("imageType") String imageType, @Context HttpServletRequest request, InputStream fileInputStream);
	
	@POST
	@Path("/create/answer/{questionId}/{imageType}")
	@Consumes("application/octet-stream")
	Response createAnswerImage(@PathParam("questionId") Long questionId, @PathParam("imageType") String imageType, @Context HttpServletRequest request, InputStream fileInputStream);
	
	@POST
	@Path("/find/question/{questionId}/{imageType}")
	@Produces("application/octet-stream")
	Response findQuestionImage(@PathParam("questionId") Long questionId, @PathParam("imageType") String imageType);
	
	@POST
	@Path("/find/answer/{questionId}/{imageType}")
	@Produces("application/octet-stream")
	Response findAnswerImage(@PathParam("questionId") Long questionId, @PathParam("imageType") String imageType);
	
	public boolean buildImageHolder(ImageHolder imageHolder, String imageType, InputStream source);
}
