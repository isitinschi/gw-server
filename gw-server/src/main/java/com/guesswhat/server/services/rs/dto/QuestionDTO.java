package com.guesswhat.server.services.rs.dto;

import com.guesswhat.server.persistence.jpa.entity.Question;

public class QuestionDTO {

	private String id;
	private String answer1;
	private String answer2;
	private String answer3;
	private String answer4;
	private String correctAnswer;
	
	public QuestionDTO() {
		
	}

	public QuestionDTO(Question question) {
		super();
		this.id = String.valueOf(question.getId());
		this.answer1 = question.getAnswer1();
		this.answer2 = question.getAnswer2();
		this.answer3 = question.getAnswer3();
		this.answer4 = question.getAnswer4();
		this.correctAnswer = question.getCorrectAnswer();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

		
}
