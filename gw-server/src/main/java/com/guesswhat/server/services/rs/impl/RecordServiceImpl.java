package com.guesswhat.server.services.rs.impl;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Path;

import com.guesswhat.server.persistence.jpa.cfg.EntityFactory;
import com.guesswhat.server.persistence.jpa.entity.Record;
import com.guesswhat.server.services.rs.dto.RecordDTO;
import com.guesswhat.server.services.rs.face.RecordService;

@Path("/records")
public class RecordServiceImpl implements RecordService {

	@Override
	@RolesAllowed("READER")
	public void saveUserRecord(RecordDTO recordDTO) {
		Record record = new Record(recordDTO);
		EntityFactory.getInstance().getRecordDAO().save(record);
	}

	@Override
	@RolesAllowed("READER")
	public String findTopRecords() {
		// TODO Auto-generated method stub
		return null;
	}

}

