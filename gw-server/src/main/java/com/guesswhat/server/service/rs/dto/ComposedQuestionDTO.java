package com.guesswhat.server.service.rs.dto;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ComposedQuestionDTO implements Serializable {

	private QuestionDTO questionDTO;
	private ImageDTO imageQuestionDTO;
	private ImageDTO imageAnswerDTO;
	
	public ComposedQuestionDTO() {
		
	}

	public QuestionDTO getQuestionDTO() {
		return questionDTO;
	}

	public void setQuestionDTO(QuestionDTO questionDTO) {
		this.questionDTO = questionDTO;
	}

	public ImageDTO getImageQuestionDTO() {
		return imageQuestionDTO;
	}

	public void setImageQuestionDTO(ImageDTO imageQuestionDTO) {
		this.imageQuestionDTO = imageQuestionDTO;
	}

	public ImageDTO getImageAnswerDTO() {
		return imageAnswerDTO;
	}

	public void setImageAnswerDTO(ImageDTO imageAnswerDTO) {
		this.imageAnswerDTO = imageAnswerDTO;
	}
		
}
