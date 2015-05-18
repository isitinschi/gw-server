package com.guesswhat.server.service.rs.impl;

import java.util.Arrays;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.appengine.api.datastore.Blob;
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

	private static final int IMAGE_MAX_SIZE = 1000000; // bytes in 1 mb
	
	@Autowired private QuestionDAO questionDAO;
	@Autowired private ImageHolderDAO imageHolderDAO;
	@Autowired private ImageDAO imageDAO;

	@Override
	public void buildImageHolder(ImageHolder imageHolder, String imageType, byte [] bytes) {
		Image image = saveImage(bytes);
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

	private Image saveImage(byte [] bytes) {
		if (bytes.length < IMAGE_MAX_SIZE) {
			Blob blob = new Blob(bytes);
			Image image = new Image(blob);
			imageDAO.save(image);
			
			return image;
		} else {
			// Workaround for big files
			byte[] bytes1 = Arrays.copyOfRange(bytes, 0, IMAGE_MAX_SIZE);		
			byte[] bytes2 = Arrays.copyOfRange(bytes, IMAGE_MAX_SIZE,  bytes.length);
			
			Blob blob1 = new Blob(bytes1);
			Image image = new Image(blob1);
			
			Image image2 = saveImage(bytes2);
			image.setSecondPart(image2.getId());
			imageDAO.save(image);
			
			return image;
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
		byte [] bytes = getImageBytes(image);		
		return bytes;
	}

	private byte[] getImageBytes(Image image) {
		byte [] bytes = image.getImage().getBytes();
		if (image.getSecondPart() != null) {
			Image imageSecondPart = imageDAO.find(image.getSecondPart());
			bytes = concat(bytes, getImageBytes(imageSecondPart));
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

