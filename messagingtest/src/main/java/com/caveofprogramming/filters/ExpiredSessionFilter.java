package com.caveofprogramming.filters;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.filter.OncePerRequestFilter;

public class ExpiredSessionFilter extends OncePerRequestFilter {

	private final static String FILTER_APPLIED = "_expired_session_filter_applied";

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		/*
		if (request instanceof HttpServletRequest) {
			String url = ((HttpServletRequest) request).getRequestURL().toString();
			System.out.println("Custom session filter, url: " + url);
		} else {
			System.out.println("Running custom session filter, no url");
		}
		*/

		boolean isValidSession = request.isRequestedSessionIdValid();

		if (isValidSession) {
			filterChain.doFilter(request, response);
		} else {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		}
	}

}
