package com.guesswhat.server.service.rs.dto;

import java.io.Serializable;

public class RecordDTO implements Serializable {

	private static final long serialVersionUID = 7913643526781495014L;
	
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
