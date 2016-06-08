package com.caveofprogramming.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.caveofprogramming.model.StatusUpdate;
import com.caveofprogramming.service.StatusUpdateService;

@Controller
public class PageController {
	
	@Autowired
	private StatusUpdateService statusUpdateService;
	
	@RequestMapping("/")
	String home() {
		return "app.homepage";
	}

	@RequestMapping("/about")
	String about() {
		return "app.about";
	}

	@RequestMapping(value = "/addstatus", method = RequestMethod.GET)
	ModelAndView addStatus(ModelAndView modelAndView) {

		modelAndView.setViewName("app.addStatus");

		StatusUpdate statusUpdate = new StatusUpdate();

		modelAndView.getModel().put("statusUpdate", statusUpdate);

		return modelAndView;
	}

	@RequestMapping(value = "/addstatus", method = RequestMethod.POST)
	ModelAndView addStatus(ModelAndView modelAndView, StatusUpdate statusUpdate) {

		modelAndView.setViewName("app.addStatus");
		
		statusUpdateService.save(statusUpdate);

		return modelAndView;
	}
}
