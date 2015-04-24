package com.guesswhat.server.services.rs.backup.dto;

import java.io.Serializable;

@SuppressWarnings("serial")
public class UserBackupDTO implements Serializable {

	private String username;
	private String password;
	private String role;
	
	public UserBackupDTO() {}
	
	public UserBackupDTO(String username, String password, String role) {
		super();
		this.username = username;
		this.password = password;
		this.role = role;
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	
}
