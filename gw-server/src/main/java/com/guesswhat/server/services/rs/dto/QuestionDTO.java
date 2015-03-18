package com.guesswhat.server.services.rs.dto;

public class QuestionDTO {

	private String imageBefor;
	private String imageAfter;
	private String [] answers;
	private String correctAnswer;
	
	public QuestionDTO() {
		
	}
	
	public QuestionDTO(String imageBefor, String imageAfter, String[] answers,
			String correctAnswer) {
		super();
		this.imageBefor = imageBefor;
		this.imageAfter = imageAfter;
		this.answers = answers;
		this.correctAnswer = correctAnswer;
	}

	public String getImageBefor() {
		return imageBefor;
	}

	public void setImageBefor(String imageBefor) {
		this.imageBefor = imageBefor;
	}

	public String getImageAfter() {
		return imageAfter;
	}

	public void setImageAfter(String imageAfter) {
		this.imageAfter = imageAfter;
	}

	public String[] getAnswers() {
		return answers;
	}

	public void setAnswers(String[] answers) {
		this.answers = answers;
	}

	public String getCorrectAnswer() {
		return correctAnswer;
	}

	public void setCorrectAnswer(String correctAnswer) {
		this.correctAnswer = correctAnswer;
	}	
	
}
