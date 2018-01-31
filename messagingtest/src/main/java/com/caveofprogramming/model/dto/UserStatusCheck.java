package com.caveofprogramming.model.dto;

public class UserStatusCheck {
	private Boolean isValidSession;
	private Boolean isAuthenticated;
	private int sessionTimeout;

	public UserStatusCheck(int sessionTimeout, Boolean isValidSession, Boolean isAuthenticated) {
		this.isValidSession = isValidSession;
		this.isAuthenticated = isAuthenticated;
		this.sessionTimeout = sessionTimeout;
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

	public int getSessionTimeout() {
		return sessionTimeout;
	}

}
