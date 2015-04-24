package com.guesswhat.server.services.rs.backup.dto;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ImageBackupDTO implements Serializable {

	private byte [] bytes;

	public ImageBackupDTO() {}
	
	public ImageBackupDTO(byte[] bytes) {
		super();
		this.bytes = bytes;
	}

	public byte[] getBytes() {
		return bytes;
	}

	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}	
	
}
