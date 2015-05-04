package com.guesswhat.server.service.rs.impl;

import java.util.Arrays;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.appengine.api.datastore.Blob;
import com.google.apphosting.api.ApiProxy.RequestTooLargeException;
import com.guesswhat.server.persistence.jpa.dao.ImageDAO;
import com.guesswhat.server.persistence.jpa.dao.ImageHolderDAO;
import com.guesswhat.server.persistence.jpa.dao.QuestionDAO;
import com.guesswhat.server.persistence.jpa.entity.Image;
import com.guesswhat.server.persistence.jpa.entity.ImageHolder;
import com.guesswhat.server.persistence.jpa.entity.Question;
import com.guesswhat.server.service.rs.dto.ImageType;
import com.guesswhat.server.service.rs.face.ImageService;

@Path("/images")
public class ImageServiceImpl implements ImageService {

	@Autowired private QuestionDAO questionDAO;
	@Autowired private ImageHolderDAO imageHolderDAO;
	@Autowired private ImageDAO imageDAO;

	@Override
	public void buildImageHolder(ImageHolder imageHolder, String imageType, byte [] bytes) {
	    Blob blob = new Blob(bytes);
		
	    Image image = new Image(blob);
	    
	    try {
	    	imageDAO.save(image);
	    } catch (RequestTooLargeException e) {
	    	// file is too big. Workaround:
			int middle = bytes.length / 2;
			byte[] bytes1 = Arrays.copyOfRange(bytes, 0, middle);
			byte[] bytes2 = Arrays.copyOfRange(bytes, middle,  bytes.length);
			Blob blob1 = new Blob(bytes1);
			Blob blob2 = new Blob(bytes2);
			image = new Image(blob1);
			Image image2 = new Image(blob2);
			imageDAO.save(image2);
			image.setSecondPart(image2.getId());
			// try again :)
			imageDAO.save(image);
	    }
	    Long imageId = image.getId();
	    
	    switch (ImageType.valueOf(imageType)) {
	    	case XXHDPI:	imageHolder.setXxhdpiImageId(imageId);
	    					break;
	    	case XHDPI:		imageHolder.setXhdpiImageId(imageId);
							break;
	    	case HDPI:		imageHolder.setHdpiImageId(imageId);
							break;
	    	case MDPI:		imageHolder.setMdpiImageId(imageId);
							break;
	    	case LDPI:		imageHolder.setLdpiImageId(imageId);
							break;
	    	default:   		return;
	    }			
	}

	@Override
	@RolesAllowed("READER")
	public Response findQuestionImage(Long questionId, String imageType) {
		Question question = questionDAO.find(questionId);
		if (question != null) {
			Long imageHolderId = question.getImageQuestionId();
			if (imageHolderId != null) {
				byte [] bytes = findImage(imageHolderId, imageType);
				
				if (bytes != null) {
					return Response.ok(bytes).build();				
				}
			}
		}

		return Response.ok().build();
	}

	@Override
	@RolesAllowed("READER")
	public Response findAnswerImage(Long questionId, String imageType) {
		Question question = questionDAO.find(questionId);
		if (question != null) {
			Long imageHolderId = question.getImageAnswerId();
			if (imageHolderId != null) {
				byte [] bytes = findImage(imageHolderId, imageType);
				
				if (bytes != null) {
					return Response.ok(bytes).build();			
				}
			}			
		}

		return Response.ok().build();
	}
	
	private byte[] findImage(Long imageHolderId, String imageType) {
		ImageHolder imageHolder = imageHolderDAO.find(imageHolderId);
		Long imageId = null;
		
		switch (ImageType.valueOf(imageType)) {
	    	case XXHDPI:	imageId = imageHolder.getXxhdpiImageId();
	    					break;
	    	case XHDPI:		imageId = imageHolder.getXhdpiImageId();
							break;
	    	case HDPI:		imageId = imageHolder.getHdpiImageId();
							break;
	    	case MDPI:		imageId = imageHolder.getMdpiImageId();
							break;
	    	case LDPI:		imageId = imageHolder.getLdpiImageId();
							break;
	    	default:   		return null;
		}
		
		if (imageId == null) {
			return null;
		}
		
		return findImageById(imageId);
	}

	@Override
	public byte[] findImageById(Long imageId) {
		Image image = imageDAO.find(imageId);
		Image imageSecondPart = null;
		if (image.getSecondPart() != null) {
			imageSecondPart = imageDAO.find(image.getSecondPart());
		}
		
		byte [] bytes = null;
		if (imageSecondPart != null) {
			bytes = concat(image.getImage().getBytes(), imageSecondPart.getImage().getBytes());
		} else {
			bytes = image.getImage().getBytes();
		}
		
		return bytes;
	}

	private byte[] concat(byte[] a, byte[] b) {
		   int aLen = a.length;
		   int bLen = b.length;
		   byte[] c = new byte[aLen + bLen];
		   System.arraycopy(a, 0, c, 0, aLen);
		   System.arraycopy(b, 0, c, aLen, bLen);
		   return c;
	}

	public void setQuestionDAO(QuestionDAO questionDAO) {
		this.questionDAO = questionDAO;
	}

	public void setImageHolderDAO(ImageHolderDAO imageHolderDAO) {
		this.imageHolderDAO = imageHolderDAO;
	}

	public void setImageDAO(ImageDAO imageDAO) {
		this.imageDAO = imageDAO;
	}

}

