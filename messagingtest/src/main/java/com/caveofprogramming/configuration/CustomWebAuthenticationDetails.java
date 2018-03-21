package com.caveofprogramming.configuration;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.web.authentication.WebAuthenticationDetails;

public class CustomWebAuthenticationDetails extends WebAuthenticationDetails {

	private static final long serialVersionUID = 1L;
	
	private boolean stayLoggedIn = false;
	
	public CustomWebAuthenticationDetails(HttpServletRequest request) {
		super(request);
		
		String stayLoggedInValue = request.getParameter("stayloggedin");
		
		if(stayLoggedInValue != null) {
			stayLoggedIn = true;
		}
	}

	public boolean isStayLoggedIn() {
		return stayLoggedIn;
	}
}
