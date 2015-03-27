package com.guesswhat.server.services.rs.dto;

public enum ImageType {
	XXHDPI("XXHDPI"), XHDPI("XHDPI"), HDPI("HDPI"), MDPI("MDPI"), LDPI("LDPI");
	
	private String type;
	
	ImageType(String type) {
		this.type = type;
	}
	
	public String toString() {
		return type;
	}
}
