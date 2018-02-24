package com.caveofprogramming.filters;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.filter.OncePerRequestFilter;

public class ExpiredSessionFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		HttpSession session = request.getSession(true);
		boolean isValidSession = request.isRequestedSessionIdValid();
		boolean isNewSession = session.isNew();
		
		System.err.println(request.getRequestURI());
		System.err.println("ExpiredSessionFilter: new session: " + isNewSession );

		if (isValidSession) {
			filterChain.doFilter(request, response);
		} else {
			System.err.println("Unauthorised");
			// TODO better use 401 instead.
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		}
	}

}
