package com.caveofprogramming.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.caveofprogramming.model.dto.SearchResult;
import com.caveofprogramming.service.SearchService;

@Controller
public class SearchController {
	
	@Autowired
	SearchService searchService;

	@RequestMapping(value="/search", method=RequestMethod.POST)
	public ModelAndView search(ModelAndView modelAndView, @RequestParam("s") String text) {
		
		List<SearchResult> results = searchService.search(text);
		
		modelAndView.getModel().put("results", results);
		modelAndView.setViewName("app.search");
		
		return modelAndView;
	}
}
