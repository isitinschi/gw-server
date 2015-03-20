package com.guesswhat.server.persistence.jpa.entity;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Blob;
import com.guesswhat.server.services.rs.dto.QuestionDTO;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class QuestionIncubator {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id;
	@Persistent
	private String imageNameBefore;
	@Persistent
	private String imageNameAfter;
	@Persistent
	private Blob imageBefore;
	@Persistent
	private Blob imageAfter;
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

	public QuestionIncubator(Long id, String imageNameBefore,
			String imageNameAfter, Blob imageBefore, Blob imageAfter,
			String answer1, String answer2, String answer3, String answer4,
			String correctAnswer) {
		super();
		this.id = id;
		this.imageNameBefore = imageNameBefore;
		this.imageNameAfter = imageNameAfter;
		this.imageBefore = imageBefore;
		this.imageAfter = imageAfter;
		this.answer1 = answer1;
		this.answer2 = answer2;
		this.answer3 = answer3;
		this.answer4 = answer4;
		this.correctAnswer = correctAnswer;
	}

	public QuestionIncubator(QuestionDTO questionDTO) {
		this.imageNameBefore = questionDTO.getImageNameBefore();
		this.imageNameAfter = questionDTO.getImageNameAfter();
		this.answer1 = questionDTO.getAnswer1();
		this.answer2 = questionDTO.getAnswer2();
		this.answer3 = questionDTO.getAnswer3();
		this.answer4 = questionDTO.getAnswer4();
		this.correctAnswer = questionDTO.getCorrectAnswer();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getImageNameBefore() {
		return imageNameBefore;
	}

	public void setImageNameBefore(String imageNameBefore) {
		this.imageNameBefore = imageNameBefore;
	}

	public String getImageNameAfter() {
		return imageNameAfter;
	}

	public void setImageNameAfter(String imageNameAfter) {
		this.imageNameAfter = imageNameAfter;
	}

	public Blob getImageBefore() {
		return imageBefore;
	}

	public void setImageBefore(Blob imageBefore) {
		this.imageBefore = imageBefore;
	}

	public Blob getImageAfter() {
		return imageAfter;
	}

	public void setImageAfter(Blob imageAfter) {
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
