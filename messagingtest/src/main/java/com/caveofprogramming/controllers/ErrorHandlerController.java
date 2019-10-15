package com.caveofprogramming.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ErrorHandlerController implements ErrorController {

	@RequestMapping("/error")
	public ModelAndView errorHandler(HttpServletRequest req) {

		ModelAndView modelAndView = new ModelAndView();

		modelAndView.getModel().put("message", "Unknown error");
		modelAndView.getModel().put("url", req.getRequestURL());

		modelAndView.setViewName("app.exception");

		return modelAndView;
	}

	@Override
	public String getErrorPath() {
		return "/error";
	}

}
