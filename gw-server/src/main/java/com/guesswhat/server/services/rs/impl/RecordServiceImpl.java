package com.guesswhat.server.services.rs.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import com.guesswhat.server.persistence.jpa.cfg.EntityFactory;
import com.guesswhat.server.persistence.jpa.entity.Record;
import com.guesswhat.server.services.rs.dto.RecordDTO;
import com.guesswhat.server.services.rs.face.RecordService;

@Path("/records")
public class RecordServiceImpl implements RecordService {

	@Override
	@RolesAllowed("READER")
	public Response saveUserRecord(RecordDTO recordDTO) {
		Record record = EntityFactory.getInstance().getRecordDAO().findByUserId(recordDTO.getUserId());
		if (record != null) {
			if (record.getPoints() < recordDTO.getPoints()) {
				record.setPoints(recordDTO.getPoints());
				EntityFactory.getInstance().getRecordDAO().update(record);
			}
		} else {
			Record newRecord = new Record(recordDTO.getUserId(), recordDTO.getPoints());
			EntityFactory.getInstance().getRecordDAO().save(newRecord);			
		}		
		
		return Response.ok().build();
	}

	@Override
	@RolesAllowed("READER")
	public Response findTopRecords() {
		List<Record> records = EntityFactory.getInstance().getRecordDAO().findTop();
		List<RecordDTO> recordDTOList = new ArrayList<RecordDTO>();
		for (Record record : records) {
			RecordDTO recordDTO = new RecordDTO();
			recordDTO.setUserId(record.getUserId());
			recordDTO.setPoints(record.getPoints());
			recordDTOList.add(recordDTO);
		}
		return Response.ok(recordDTOList).build();
	}
	
	@Override
	@RolesAllowed("READER")
	public Response findUserPlace(String userId) {
		Record record = EntityFactory.getInstance().getRecordDAO().findByUserId(userId);
		
		int userPlace = 0;
		if (record != null) {
			userPlace = EntityFactory.getInstance().getRecordDAO().findRecordPlace(record);
		}
		
		return Response.ok(userPlace).build();
	}

}

