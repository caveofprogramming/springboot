package com.caveofprogramming.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class MailConfig {
	
	@Value("${mail.smtp.host}")
	private String host;
	
	@Value("${mail.smtp.port}")
	private Integer port;
	
	@Value("${mail.smtp.user}")
	private String user;
	
	@Value("${mail.smtp.pass}")
	private String password;
	
	@Bean
	public JavaMailSender mailSender() {
		
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		
		mailSender.setHost(host);
		mailSender.setPort(port);
		mailSender.setUsername(user);
		mailSender.setPassword(password);
		
		return mailSender;
	}
}
