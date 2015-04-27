package com.guesswhat.server.services.rs.face;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import com.guesswhat.server.persistence.jpa.entity.ImageHolder;

public interface ImageService {
	
	@POST
	@Path("/find/question/{questionId}/{imageType}")
	@Produces("application/octet-stream")
	Response findQuestionImage(@PathParam("questionId") Long questionId, @PathParam("imageType") String imageType);
	
	@POST
	@Path("/find/answer/{questionId}/{imageType}")
	@Produces("application/octet-stream")
	Response findAnswerImage(@PathParam("questionId") Long questionId, @PathParam("imageType") String imageType);
	
	public void buildImageHolder(ImageHolder imageHolder, String imageType, byte [] source);
}
