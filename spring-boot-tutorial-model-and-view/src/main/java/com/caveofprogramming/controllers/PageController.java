package com.caveofprogramming.controllers;

import java.util.Date;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.caveofprogramming.model.StatusUpdate;

@Controller
public class PageController {
	@RequestMapping("/")
	String home() {
		return "app.homepage";
	}
	
	@RequestMapping("/about")
	String about() {
		return "app.about";
	}
	
	@RequestMapping("/addstatus")
	ModelAndView addStatus(ModelAndView modelAndView) {
		
		modelAndView.setViewName("app.addStatus");
		
		StatusUpdate statusUpdate = new StatusUpdate();
		
		modelAndView.getModel().put("statusUpdate", statusUpdate);
		
		return modelAndView;
	}
}
