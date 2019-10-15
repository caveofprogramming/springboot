package com.caveofprogramming.configuration;

import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;


@Configuration
@Order(SecurityProperties.BASIC_AUTH_ORDER - 6)   
@EnableWebSecurity
public class AjaxSecurityConfig  extends WebSecurityConfigurerAdapter {
	
	/*
	 * This allows us to return 403 status (forbidden) if session timeout occurs,
	 * rather than the AJAX client getting redirected to the login page.
	 * 401 (unauthorized) would seem more appropriate, but apparently
	 * may cause popups asking for username and password in some browsers.
	 * 
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {

		// @formatter:off
		
		http
			.antMatcher("/ajax/*")
				.authorizeRequests()
					.anyRequest()
					.authenticated()
					.and()
					.addFilterBefore(new com.caveofprogramming.filters.ExpiredSessionFilter(), BasicAuthenticationFilter.class);
				
		// @formatter:on

	}

	
}
