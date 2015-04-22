package com.guesswhat.server.services.rs.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;

import com.guesswhat.server.persistence.jpa.dao.RecordDAO;
import com.guesswhat.server.persistence.jpa.entity.Record;
import com.guesswhat.server.services.rs.dto.RecordDTO;
import com.guesswhat.server.services.rs.face.RecordService;

@Path("/records")
public class RecordServiceImpl implements RecordService {

	@Autowired private RecordDAO recordDAO;
	
	@Override
	@RolesAllowed("READER")
	public Response saveUserRecord(RecordDTO recordDTO) {
		Record record = recordDAO.findByUserId(recordDTO.getUserId());
		if (record != null) {
			if (record.getPoints() < recordDTO.getPoints()) {
				record.setPoints(recordDTO.getPoints());
				recordDAO.update(record);
			}
		} else {
			Record newRecord = new Record(recordDTO.getUserId(), recordDTO.getPoints());
			recordDAO.save(newRecord);			
		}		
		
		return Response.ok().build();
	}

	@Override
	@RolesAllowed("READER")
	public Response findTopRecords() {
		List<Record> records = recordDAO.findTop();
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
		Record record = recordDAO.findByUserId(userId);
		
		int userPlace = 0;
		if (record != null) {
			userPlace = recordDAO.findRecordPlace(record);
		}
		
		return Response.ok(userPlace).build();
	}

	public void setRecordDAO(RecordDAO recordDAO) {
		this.recordDAO = recordDAO;
	}

}

