package com.guesswhat.server.services.rs.dto;

public class RecordDTO {

	private String userId;
	private int points;
	
	public RecordDTO() {
		
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
