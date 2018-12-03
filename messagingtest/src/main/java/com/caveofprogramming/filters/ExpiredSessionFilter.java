package com.caveofprogramming.filters;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;

public class ExpiredSessionFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		System.err.println("Filter checking if session expired.");

		boolean isValidSession = request.isRequestedSessionIdValid();

		if (isValidSession) {
			filterChain.doFilter(request, response);
		} else {
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		}
	}

}
