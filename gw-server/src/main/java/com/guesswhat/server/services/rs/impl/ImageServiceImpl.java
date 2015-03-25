package com.guesswhat.server.services.rs.impl;

import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.Path;

import org.apache.commons.io.IOUtils;

import com.google.appengine.api.datastore.Blob;
import com.guesswhat.server.persistence.jpa.cfg.EntityFactory;
import com.guesswhat.server.persistence.jpa.dao.ImageType;
import com.guesswhat.server.persistence.jpa.entity.Image;
import com.guesswhat.server.persistence.jpa.entity.ImageHolder;
import com.guesswhat.server.persistence.jpa.entity.Question;
import com.guesswhat.server.persistence.jpa.entity.QuestionIncubator;
import com.guesswhat.server.services.rs.face.ImageService;
import com.sun.jersey.multipart.BodyPartEntity;
import com.sun.jersey.multipart.MultiPart;

@Path("/images")
public class ImageServiceImpl implements ImageService {

	@Override
	public void createQuestionImage(Long questionId, String imageType, MultiPart multiPart) {
		QuestionIncubator questionIncubator = EntityFactory.getInstance().getQuestionIncubatorDAO().find(questionId);
		if (questionIncubator.getImageQuestion() == null) {
			ImageHolder imageHolder = new ImageHolder();
			questionIncubator.setImageQuestion(imageHolder);
			EntityFactory.getInstance().getQuestionIncubatorDAO().update(questionIncubator);
		}
		buildImageHolder(questionIncubator.getImageQuestion(), imageType, multiPart);
		buildQuestion(questionIncubator);		
	}
	
	@Override
	public void createAnswerImage(Long questionId, String imageType, MultiPart multiPart) {
		QuestionIncubator questionIncubator = EntityFactory.getInstance().getQuestionIncubatorDAO().find(questionId);
		if (questionIncubator.getImageAnswer() == null) {
			ImageHolder imageHolder = new ImageHolder();
			questionIncubator.setImageAnswer(imageHolder);
			EntityFactory.getInstance().getQuestionIncubatorDAO().update(questionIncubator);
		}
		buildImageHolder(questionIncubator.getImageAnswer(), imageType, multiPart);
		buildQuestion(questionIncubator);	
	}

	private void buildImageHolder(ImageHolder imageHolder, String imageType, MultiPart multiPart) {
		BodyPartEntity bpe = (BodyPartEntity) multiPart.getBodyParts().get(0).getEntity();
	    InputStream source = bpe.getInputStream();
	    Blob blob = null;
	    try {
			blob = new Blob(IOUtils.toByteArray(source));
		} catch (IOException e) {
			e.printStackTrace();
		}
	    Image image = new Image(blob);
	    EntityFactory.getInstance().getImageDAO().save(image);
	    
	    switch(ImageType.valueOf(imageType)) {
	    	case XXHDPI:	imageHolder.setXxhdpiImage(image);
	    					break;
	    	case XHDPI:		imageHolder.setXhdpiImage(image);
							break;
	    	case HDPI:		imageHolder.setHdpiImage(image);
							break;
	    	case MDPI:		imageHolder.setMdpiImage(image);
							break;
	    	case LDPI:		imageHolder.setLdpiImage(image);
							break;
	    }
	    
	    EntityFactory.getInstance().getImageHolderDAO().update(imageHolder);
	}
	
	private void buildQuestion(QuestionIncubator questionIncubator) {
		if (questionIncubator.getImageQuestion() != null && questionIncubator.getImageAnswer() != null) {
			if (questionIncubator.getImageQuestion().isFull() && questionIncubator.getImageAnswer().isFull()) {
				Question question = new Question(questionIncubator);
				EntityFactory.getInstance().getQuestionDAO().save(question);
			}
		}
	}

	@Override
	public String findQuestionImage(Long questionId, String imageType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String findAnswerImage(Long questionId, String imageType) {
		// TODO Auto-generated method stub
		return null;
	}

}

