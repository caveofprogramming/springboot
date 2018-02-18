package com.caveofprogramming.configuration;

import java.util.EnumSet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.SessionTrackingMode;

import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Configuration;

/*
 * Stop JSESSIONID appearing in URL by insisting on cookies being used.
 */

@Configuration
public class WebAppInitializer implements ServletContextInitializer {

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
    	servletContext.setSessionTrackingModes(EnumSet.of(SessionTrackingMode.COOKIE));
    }

}