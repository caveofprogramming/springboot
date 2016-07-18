package com.caveofprogramming.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.caveofprogramming.model.Profile;

@Controller
public class ProfileController {

	@RequestMapping(value="/profile")
	public ModelAndView showProfile(ModelAndView modelAndView) {
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username = auth.getName();
		
		Profile profile = new Profile();
		
		modelAndView.getModel().put("profile", profile);
		
		modelAndView.setViewName("app.profile");
		return modelAndView;
	}
}
