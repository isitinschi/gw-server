package com.guesswhat.server.services.rs.backup.dto;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public class BackupDTO implements Serializable {

	private List<QuestionBackupDTO> questionBackupDTOList;
	private List<UserBackupDTO> userBackupDTOList;
	private List<RecordBackupDTO> recordBackupDTOList;
	
	public BackupDTO() {
		
	}

	public List<QuestionBackupDTO> getQuestionBackupDTOList() {
		return questionBackupDTOList;
	}

	public void setQuestionBackupDTOList(
			List<QuestionBackupDTO> questionBackupDTOList) {
		this.questionBackupDTOList = questionBackupDTOList;
	}

	public List<UserBackupDTO> getUserBackupDTOList() {
		return userBackupDTOList;
	}

	public void setUserBackupDTOList(List<UserBackupDTO> userBackupDTOList) {
		this.userBackupDTOList = userBackupDTOList;
	}

	public List<RecordBackupDTO> getRecordBackupDTOList() {
		return recordBackupDTOList;
	}

	public void setRecordBackupDTOList(List<RecordBackupDTO> recordBackupDTOList) {
		this.recordBackupDTOList = recordBackupDTOList;
	}
		
}
