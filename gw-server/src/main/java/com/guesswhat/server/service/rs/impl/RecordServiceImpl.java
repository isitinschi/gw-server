package com.guesswhat.server.service.rs.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;

import com.guesswhat.server.persistence.jpa.dao.RecordDAO;
import com.guesswhat.server.persistence.jpa.entity.Record;
import com.guesswhat.server.service.rs.dto.RecordDTO;
import com.guesswhat.server.service.rs.dto.RecordDTOListWrapper;
import com.guesswhat.server.service.rs.face.DatabaseService;
import com.guesswhat.server.service.rs.face.RecordService;

@Path("/records")
public class RecordServiceImpl implements RecordService {

	@Autowired private DatabaseService databaseService;
	
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

	@Override
	@RolesAllowed("WRITER")
	public Response downloadBackup() {
		List<Record> records = recordDAO.findAll();
		List<RecordDTO> recordDTOList = new ArrayList<RecordDTO>();
		for (Record record : records) {
			RecordDTO userBackupDTO = new RecordDTO(record.getUserId(), record.getPoints());
			recordDTOList.add(userBackupDTO);
		}
		
		RecordDTOListWrapper recordDTOListWrapper = new RecordDTOListWrapper(recordDTOList);
		return Response.ok(recordDTOListWrapper).build();
	}

	@Override
	@RolesAllowed("WRITER")
	public Response uploadBackup(RecordDTOListWrapper recordDTOListWrapper) {
		recordDAO.removeAll();
		
		for (RecordDTO recordBackupDTO : recordDTOListWrapper.getRecordDTOList()) {
			Record record = new Record(recordBackupDTO.getUserId(), recordBackupDTO.getPoints());
			recordDAO.save(record);
		}
		databaseService.incrementVersion();
		
		return Response.ok().build();
	}
	
	@Override
	@RolesAllowed("WRITER")
	public Response deleteRecords() {
		recordDAO.removeAll();
		databaseService.incrementVersion();
		
		return Response.ok().build();
	}
	
	public void setDatabaseService(DatabaseService databaseService) {
		this.databaseService = databaseService;
	}
	
	public void setRecordDAO(RecordDAO recordDAO) {
		this.recordDAO = recordDAO;
	}

}

