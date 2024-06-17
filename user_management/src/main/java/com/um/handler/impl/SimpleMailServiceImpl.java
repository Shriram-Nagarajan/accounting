package com.um.handler.impl;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.um.handler.EmailService;
import com.um.model.EmailMessage;

@Service("simpleMailService")
public class SimpleMailServiceImpl implements EmailService {

    private JavaMailSender javaMailSender;
    
    public SimpleMailServiceImpl(JavaMailSender mailSender) {
    	this.javaMailSender = mailSender;
    }
	
	@Override
	public void sendEmail(EmailMessage emailMessage) {
		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
		simpleMailMessage.setFrom(emailMessage.getFromAddress());
		simpleMailMessage.setTo(emailMessage.getToAddress());
		simpleMailMessage.setSubject(emailMessage.getSubject());
		simpleMailMessage.setText(emailMessage.getBody());
		javaMailSender.send(simpleMailMessage);		
	}

}
