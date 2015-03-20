package com.guesswhat.server.services.rs.impl;

import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.Path;

import org.apache.commons.io.IOUtils;

import com.google.appengine.api.datastore.Blob;
import com.guesswhat.server.persistence.jpa.cfg.EntityFactory;
import com.guesswhat.server.persistence.jpa.entity.Question;
import com.guesswhat.server.persistence.jpa.entity.QuestionIncubator;
import com.guesswhat.server.services.rs.dto.QuestionDTO;
import com.guesswhat.server.services.rs.face.QuestionService;
import com.sun.jersey.multipart.BodyPartEntity;
import com.sun.jersey.multipart.MultiPart;

@Path("/questions")
public class QuestionServiceImpl implements QuestionService {

	@Override
	public String findQuestion() {
		return "Hi!-)";
	}

	@Override
	public String addQuestion(QuestionDTO questionDTO) {
		QuestionIncubator questionIncubator = new QuestionIncubator(questionDTO);
		EntityFactory.getInstance().getQuestionIncubatorDAO().save(questionIncubator);
		return String.valueOf(questionIncubator.getId());
	}

	@Override
	public void addQuestionImage(Long questionId, String imageName, MultiPart multiPart) {
		BodyPartEntity bpe = (BodyPartEntity) multiPart.getBodyParts().get(0).getEntity();
	    InputStream source = bpe.getInputStream();
	    Blob blob = null;
	    try {
			blob = new Blob(IOUtils.toByteArray(source));
		} catch (IOException e) {
			e.printStackTrace();
		}
	    
	    QuestionIncubator questionIncubator = EntityFactory.getInstance().getQuestionIncubatorDAO().find(questionId);
	    if (imageName.equals(questionIncubator.getImageNameBefore())) {
	    	questionIncubator.setImageBefore(blob);
	    } else if (imageName.equals(questionIncubator.getImageNameAfter())) {
	    	questionIncubator.setImageAfter(blob);
	    }
	    
	    if (questionIncubator.getImageBefore() != null && questionIncubator.getImageAfter() != null) {
	    	Question question = new Question(questionIncubator);
	    	EntityFactory.getInstance().getQuestionDAO().save(question);
	    }
	}

}

