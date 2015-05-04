package com.guesswhat.server.service.rs.dto;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ImageDTO implements Serializable {

	private byte[] xxhdpiImageId;
	private byte[] xhdpiImageId;
	private byte[] hdpiImageId;
	private byte[] mdpiImageId;
	private byte[] ldpiImageId;
	
	public ImageDTO() {
		
	}

	public byte[] getXxhdpiImageId() {
		return xxhdpiImageId;
	}

	public void setXxhdpiImageId(byte[] xxhdpiImageId) {
		this.xxhdpiImageId = xxhdpiImageId;
	}

	public byte[] getXhdpiImageId() {
		return xhdpiImageId;
	}

	public void setXhdpiImageId(byte[] xhdpiImageId) {
		this.xhdpiImageId = xhdpiImageId;
	}

	public byte[] getHdpiImageId() {
		return hdpiImageId;
	}

	public void setHdpiImageId(byte[] hdpiImageId) {
		this.hdpiImageId = hdpiImageId;
	}

	public byte[] getMdpiImageId() {
		return mdpiImageId;
	}

	public void setMdpiImageId(byte[] mdpiImageId) {
		this.mdpiImageId = mdpiImageId;
	}

	public byte[] getLdpiImageId() {
		return ldpiImageId;
	}

	public void setLdpiImageId(byte[] ldpiImageId) {
		this.ldpiImageId = ldpiImageId;
	}	
		
}
