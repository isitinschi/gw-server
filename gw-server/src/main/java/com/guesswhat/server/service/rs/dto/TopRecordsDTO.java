package com.guesswhat.server.service.rs.dto;

import java.util.List;

public class TopRecordsDTO {

	private List<Integer> records;
	private int userPlace;
	
	public TopRecordsDTO() {
		
	}

	public List<Integer> getRecords() {
		return records;
	}

	public void setRecords(List<Integer> records) {
		this.records = records;
	}

	public int getUserPlace() {
		return userPlace;
	}

	public void setUserPlace(int userPlace) {
		this.userPlace = userPlace;
	}
	
}
