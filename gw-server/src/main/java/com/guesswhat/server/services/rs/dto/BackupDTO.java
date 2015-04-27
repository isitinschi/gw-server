package com.guesswhat.server.services.rs.dto;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public class BackupDTO implements Serializable {

	private List<ComposedQuestionDTO> questionDTOList;
	private List<UserDTO> userDTOList;
	private List<RecordDTO> recordDTOList;
	
	public BackupDTO() {
		
	}

	public List<ComposedQuestionDTO> getQuestionDTOList() {
		return questionDTOList;
	}

	public void setQuestionDTOList(List<ComposedQuestionDTO> questionDTOList) {
		this.questionDTOList = questionDTOList;
	}

	public List<UserDTO> getUserDTOList() {
		return userDTOList;
	}

	public void setUserDTOList(List<UserDTO> userDTOList) {
		this.userDTOList = userDTOList;
	}

	public List<RecordDTO> getRecordDTOList() {
		return recordDTOList;
	}

	public void setRecordDTOList(List<RecordDTO> recordDTOList) {
		this.recordDTOList = recordDTOList;
	}
		
}
