package com.guesswhat.server.persistence.jpa.entity;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class Record {

	@Id
	private Long id;
	@Index
	private String userId;
	@Index
	private int points;
	
	public Record() {
		
	}

	public Record(String userId, int points) {
		super();
		this.userId = userId;
		this.points = points;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
