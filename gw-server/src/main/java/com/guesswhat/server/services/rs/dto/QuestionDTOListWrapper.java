package com.guesswhat.server.services.rs.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "questionDTOList")
public class QuestionDTOListWrapper {

	private List<QuestionDTO> questionDTOList;
	
	public QuestionDTOListWrapper() {}

	public QuestionDTOListWrapper(List<QuestionDTO> questionDTOList) {
		super();
		this.questionDTOList = questionDTOList;
	}

	public List<QuestionDTO> getQuestionDTOList() {
		return questionDTOList;
	}

	public void setQuestionDTOList(List<QuestionDTO> questionDTOList) {
		this.questionDTOList = questionDTOList;
	}
	
}
