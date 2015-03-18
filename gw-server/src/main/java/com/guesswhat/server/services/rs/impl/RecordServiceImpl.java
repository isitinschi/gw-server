package com.guesswhat.server.services.rs.impl;

import javax.ws.rs.Path;

import com.guesswhat.server.services.rs.face.RecordService;

@Path("/records")
public class RecordServiceImpl implements RecordService {

	@Override
	public void saveUserRecord(int userId, int recordPoints) {
		
	}

	@Override
	public String findTopRecords() {
		// TODO Auto-generated method stub
		return null;
	}

}

