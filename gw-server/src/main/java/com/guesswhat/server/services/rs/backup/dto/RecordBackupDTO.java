package com.guesswhat.server.services.rs.backup.dto;

import java.io.Serializable;

public class RecordBackupDTO implements Serializable {

	private String userId;
	private int points;
	
	public RecordBackupDTO() {}
	
	public RecordBackupDTO(String userId, int points) {
		super();
		this.userId = userId;
		this.points = points;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}
	
}
