package com.guesswhat.server.persistence.jpa.entity;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
public class Information {

	@Id
	private Long id;
	private int databaseVersion;
	
	public Information() {
		
	}

	public Information(int databaseVersion) {
		this.databaseVersion = databaseVersion;
	}

	public int getDatabaseVersion() {
		return databaseVersion;
	}

	public void setDatabaseVersion(int databaseVersion) {
		this.databaseVersion = databaseVersion;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
}
