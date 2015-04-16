package com.guesswhat.server.persistence.jpa.entity;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
import com.guesswhat.server.services.rs.backup.dto.QuestionBackupDTO;
import com.guesswhat.server.services.rs.dto.QuestionDTO;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Question {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;
	@Persistent
	private Key imageQuestion;
	@Persistent
	private Key imageAnswer;
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

	public Question(QuestionIncubator questionIncubator) {
		this.imageQuestion = questionIncubator.getImageQuestion();
		this.imageAnswer = questionIncubator.getImageAnswer();
		this.answer1 = questionIncubator.getAnswer1();
		this.answer2 = questionIncubator.getAnswer2();
		this.answer3 = questionIncubator.getAnswer3();
		this.answer4 = questionIncubator.getAnswer4();
		this.correctAnswer = questionIncubator.getCorrectAnswer();
	}
	
	public Question(QuestionDTO questionDTO) {
		this.answer1 = questionDTO.getAnswer1();
		this.answer2 = questionDTO.getAnswer2();
		this.answer3 = questionDTO.getAnswer3();
		this.answer4 = questionDTO.getAnswer4();
		this.correctAnswer = questionDTO.getCorrectAnswer();
	}

	public Question(QuestionBackupDTO questionBackupDTO) {
		this.answer1 = questionBackupDTO.getAnswer1();
		this.answer2 = questionBackupDTO.getAnswer2();
		this.answer3 = questionBackupDTO.getAnswer3();
		this.answer4 = questionBackupDTO.getAnswer4();
		this.correctAnswer = questionBackupDTO.getCorrectAnswer();
	}

	public Key getImageQuestion() {
		return imageQuestion;
	}

	public void setImageQuestion(Key imageQuestion) {
		this.imageQuestion = imageQuestion;
	}

	public Key getImageAnswer() {
		return imageAnswer;
	}

	public void setImageAnswer(Key imageAnswer) {
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
	
	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}
	
}
