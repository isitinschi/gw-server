package com.guesswhat.server.persistence.jpa.entity;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import com.guesswhat.server.services.rs.dto.RecordDTO;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Record extends Entity {

	private int userId;
	@Persistent
	private String userName;
	@Persistent
	private int recordPoints;
	
	public Record() {
		
	}

	public Record(int userId, String userName, int recordPoints) {
		super();
		this.userId = userId;
		this.userName = userName;
		this.recordPoints = recordPoints;
	}

	public Record(RecordDTO recordDTO) {
		this.userId = recordDTO.getUserId();
		this.userName = recordDTO.getUserName();
		this.recordPoints = recordDTO.getRecordPoints();
	}	

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getRecordPoints() {
		return recordPoints;
	}

	public void setRecordPoints(int recordPoints) {
		this.recordPoints = recordPoints;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}	
	
}
