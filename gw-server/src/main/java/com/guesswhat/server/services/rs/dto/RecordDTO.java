package com.guesswhat.server.services.rs.dto;

public class RecordDTO {

	private int userId;	
	private String userName;	
	private int recordPoints;
	
	public RecordDTO() {
		
	}

	public RecordDTO(int userId, String userName, int recordPoints) {
		super();
		this.userId = userId;
		this.userName = userName;
		this.recordPoints = recordPoints;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getRecordPoints() {
		return recordPoints;
	}

	public void setRecordPoints(int recordPoints) {
		this.recordPoints = recordPoints;
	}
	
}
