package com.guesswhat.server.persistence.jpa.entity;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.guesswhat.server.services.rs.dto.QuestionDTO;

@Entity
public class Question {

	@Id
	private Long id;
	private Long imageQuestionId;
	private Long imageAnswerId;
	private String answer1;
	private String answer2;
	private String answer3;
	private String answer4;
	private String correctAnswer;

	public Question() {

	}
	
	public Question(QuestionDTO questionDTO) {
		this.answer1 = questionDTO.getAnswer1();
		this.answer2 = questionDTO.getAnswer2();
		this.answer3 = questionDTO.getAnswer3();
		this.answer4 = questionDTO.getAnswer4();
		this.correctAnswer = questionDTO.getCorrectAnswer();
	}

	public Long getImageQuestionId() {
		return imageQuestionId;
	}

	public void setImageQuestionId(Long imageQuestionId) {
		this.imageQuestionId = imageQuestionId;
	}

	public Long getImageAnswerId() {
		return imageAnswerId;
	}

	public void setImageAnswerId(Long imageAnswerId) {
		this.imageAnswerId = imageAnswerId;
	}

	public String getAnswer1() {
		return answer1;
	}

	public void setAnswer1(String answer1) {
		this.answer1 = answer1;
	}

	public String getAnswer2() {
		return answer2;
	}

	public void setAnswer2(String answer2) {
		this.answer2 = answer2;
	}

	public String getAnswer3() {
		return answer3;
	}

	public void setAnswer3(String answer3) {
		this.answer3 = answer3;
	}

	public String getAnswer4() {
		return answer4;
	}

	public void setAnswer4(String answer4) {
		this.answer4 = answer4;
	}

	public String getCorrectAnswer() {
		return correctAnswer;
	}

	public void setCorrectAnswer(String correctAnswer) {
		this.correctAnswer = correctAnswer;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
}
