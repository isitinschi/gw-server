package com.guesswhat.server.services.rs.dto;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class QuestionDTO {

	private String imageNameBefore;
	private String imageNameAfter;
	private String answer1;
	private String answer2;
	private String answer3;
	private String answer4;
	private String correctAnswer;
	
	public QuestionDTO() {
		
	}

	public QuestionDTO(String imageNameBefore, String imageNameAfter,
			String answer1, String answer2, String answer3, String answer4,
			String correctAnswer) {
		super();
		this.imageNameBefore = imageNameBefore;
		this.imageNameAfter = imageNameAfter;
		this.answer1 = answer1;
		this.answer2 = answer2;
		this.answer3 = answer3;
		this.answer4 = answer4;
		this.correctAnswer = correctAnswer;
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
