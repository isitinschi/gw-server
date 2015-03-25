package com.guesswhat.server.persistence.jpa.entity;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import com.guesswhat.server.services.rs.dto.QuestionDTO;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class QuestionIncubator extends Entity {

	@Persistent
	private ImageHolder imageQuestion;
	@Persistent
	private ImageHolder imageAnswer;
	@Persistent
	private String answer1;
	@Persistent
	private String answer2;
	@Persistent
	private String answer3;
	@Persistent
	private String answer4;
	@Persistent
	private String correctAnswer;

	public QuestionIncubator() {

	}

	public QuestionIncubator(QuestionDTO questionDTO) {
		this.answer1 = questionDTO.getAnswer1();
		this.answer2 = questionDTO.getAnswer2();
		this.answer3 = questionDTO.getAnswer3();
		this.answer4 = questionDTO.getAnswer4();
		this.correctAnswer = questionDTO.getCorrectAnswer();
	}

	public ImageHolder getImageQuestion() {
		return imageQuestion;
	}

	public void setImageQuestion(ImageHolder imageQuestion) {
		this.imageQuestion = imageQuestion;
	}

	public ImageHolder getImageAnswer() {
		return imageAnswer;
	}

	public void setImageAnswer(ImageHolder imageAnswer) {
		this.imageAnswer = imageAnswer;
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
