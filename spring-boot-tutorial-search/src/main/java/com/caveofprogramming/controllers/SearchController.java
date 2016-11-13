package com.caveofprogramming.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class SearchController {

	@RequestMapping(value="/search", method=RequestMethod.POST)
	public ModelAndView search(ModelAndView modelAndView, @RequestParam("s") String text) {
		
		System.out.println("Search text: " + text);
		modelAndView.setViewName("app.search");
		
		return modelAndView;
	}
}
