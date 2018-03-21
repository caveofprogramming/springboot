package com.caveofprogramming.configuration;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationSuccessListener implements ApplicationListener<InteractiveAuthenticationSuccessEvent> {

	@Autowired
	private HttpSession session;

	@Override
	public void onApplicationEvent(InteractiveAuthenticationSuccessEvent event) {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		CustomWebAuthenticationDetails details = (CustomWebAuthenticationDetails) auth.getDetails();

		if (details.isStayLoggedIn()) {
			// Session should not time out at all.
			session.setMaxInactiveInterval(-1);
		}
	}
}