package com.guesswhat.server.services.rs.dto;

import java.io.Serializable;

@SuppressWarnings("serial")
public class RecordDTO implements Serializable {

	private String userId;
	private int points;
	
	public RecordDTO() {
		
	}

	public RecordDTO(String userId, int points) {
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
