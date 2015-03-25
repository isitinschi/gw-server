package com.guesswhat.server.persistence.jpa.dao;

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
