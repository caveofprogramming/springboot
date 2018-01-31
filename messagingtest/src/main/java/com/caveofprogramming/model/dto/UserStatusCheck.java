package com.caveofprogramming.model.dto;

public class UserStatusCheck {
	private Boolean isValidSession;
	private Boolean isAuthenticated;

	public UserStatusCheck(Boolean isValidSession, Boolean isAuthenticated) {
		this.isValidSession = isValidSession;
		this.isAuthenticated = isAuthenticated;
	}
	
	public Boolean isOK() {
		return isValidSession() && isAuthenticated();
	}

	public Boolean isValidSession() {
		return isValidSession;
	}

	public Boolean isAuthenticated() {
		return isAuthenticated;
	}

}
