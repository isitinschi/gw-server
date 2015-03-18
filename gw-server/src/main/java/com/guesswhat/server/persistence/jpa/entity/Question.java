package com.guesswhat.server.persistence.jpa.entity;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.guesswhat.server.services.rs.dto.QuestionDTO;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Question {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private int id;
	@Persistent
	private String imageBefore;	
	@Persistent
	private String imageAfter;	
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
	
	public Question() {
		
	}

	public Question(int id, String imageBefore, String imageAfter,
			String answer1, String answer2, String answer3, String answer4,
			String correctAnswer) {
		super();
		this.id = id;
		this.imageBefore = imageBefore;
		this.imageAfter = imageAfter;
		this.answer1 = answer1;
		this.answer2 = answer2;
		this.answer3 = answer3;
		this.answer4 = answer4;
		this.correctAnswer = correctAnswer;
	}

	public Question(QuestionDTO questionDTO) {
		this.imageBefore = questionDTO.getImageBefor();
		this.imageAfter = questionDTO.getImageAfter();
		this.answer1 = questionDTO.getAnswers()[0];
		this.answer2 = questionDTO.getAnswers()[1];
		this.answer3 = questionDTO.getAnswers()[2];
		this.answer4 = questionDTO.getAnswers()[3];
		this.correctAnswer = questionDTO.getCorrectAnswer();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getImageBefore() {
		return imageBefore;
	}

	public void setImageBefore(String imageBefore) {
		this.imageBefore = imageBefore;
	}

	public String getImageAfter() {
		return imageAfter;
	}

	public void setImageAfter(String imageAfter) {
		this.imageAfter = imageAfter;
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
