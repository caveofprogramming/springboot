package com.caveofprogramming.service;

import java.util.Date;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

	@Autowired
	private JavaMailSender mailSender;

	@Value("${mail.enable}")
	private Boolean enabled;

	private void send(MimeMessagePreparator preparator) {

		if (enabled) {
			mailSender.send(preparator);
		}
	}

	public void sendVerificationMail(String email) {
		StringBuilder sb = new StringBuilder();

		sb.append("<html>");
		sb.append("<p>Please verify your email address by visiting the following link.</p>");
		sb.append("<p><a href='#'>Click here</a></p>");
		sb.append("</html>");

		MimeMessagePreparator preparator = new MimeMessagePreparator() {

			@Override
			public void prepare(MimeMessage mimeMessage) throws Exception {

				MimeMessageHelper message = new MimeMessageHelper(mimeMessage);

				message.setTo(email);
				message.setFrom(new InternetAddress("bob@caveofprogramming.com"));
				message.setSubject("Verify Your Email Address");
				message.setSentDate(new Date());

				message.setText(sb.toString(), true);

			}

		};

		send(preparator);
	}
}
