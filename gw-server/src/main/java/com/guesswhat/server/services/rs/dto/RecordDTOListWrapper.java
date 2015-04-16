package com.guesswhat.server.services.rs.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "recordDTOList")
public class RecordDTOListWrapper {

	private List<RecordDTO> recordDTOList;
	
	public RecordDTOListWrapper() {}

	public RecordDTOListWrapper(List<RecordDTO> recordDTOList) {
		super();
		this.recordDTOList = recordDTOList;
	}

	public List<RecordDTO> getRecordDTOList() {
		return recordDTOList;
	}

	public void setRecordDTOList(List<RecordDTO> recordDTOList) {
		this.recordDTOList = recordDTOList;
	}
		
}
