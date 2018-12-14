package com.caveofprogramming.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.caveofprogramming.model.entity.SiteUser;

@Controller
public class ChatController {
	
	@Autowired
	private Util util;

	@RequestMapping("/chatview")
	ModelAndView chatView(ModelAndView modelAndView) {
		
		SiteUser thisUser = util.getUser();
		
		modelAndView.setViewName("chat.chatview");
		modelAndView.getModel().put("thisUserID", thisUser.getId());
		
		return modelAndView;
	}
}
