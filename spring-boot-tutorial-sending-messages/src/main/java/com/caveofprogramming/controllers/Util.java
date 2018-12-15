package com.caveofprogramming.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.caveofprogramming.model.entity.SiteUser;
import com.caveofprogramming.service.UserService;

@Component
public class Util {
	
	@Autowired
	private UserService userService;
	
	public SiteUser getUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		String email = auth.getName();
		
		return userService.get(email);
	}
}
