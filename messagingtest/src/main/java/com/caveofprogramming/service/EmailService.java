package com.caveofprogramming.service;

import java.util.Date;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

@Service
public class EmailService {
	
	@Autowired
	private JavaMailSender mailSender;
	
	private TemplateEngine templateEngine;
	
	@Value("${mail.enable}")
	private Boolean enable;
	
	@Value("${site.url}")
	private String url;
	
	private void send(MimeMessagePreparator preparator) {
		if(enable) {
			mailSender.send(preparator);
		}
	}
	
	@Autowired
	public EmailService(TemplateEngine templateEngine) {
		ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
		templateResolver.setPrefix("mail/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode("HTML5");
        templateResolver.setCacheable(false);
		templateEngine.setTemplateResolver(templateResolver);
		this.templateEngine = templateEngine;
	}

	@Async
	public void sendVerificationEmail(String emailAddress, String token) {
		
		Context context = new Context();
		context.setVariable("token", token);
		context.setVariable("url", url);
		
		String contents = templateEngine.process("verifyemail", context);
		
		System.out.println(contents);
		
		MimeMessagePreparator preparator = new MimeMessagePreparator() {

			@Override
			public void prepare(MimeMessage mimeMessage) throws Exception {
				
				MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
				
				message.setTo(emailAddress);
				message.setFrom(new InternetAddress("no-reply@caveofprogramming.com"));
				message.setSubject("Please Verify Your Email Address");
				message.setSentDate(new Date());
				
				message.setText(contents, true);
			}
			
		};
		
		send(preparator);
	}
}
