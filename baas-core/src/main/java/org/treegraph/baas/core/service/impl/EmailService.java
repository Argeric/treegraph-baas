package org.treegraph.baas.core.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService{
	
	@Autowired
    private JavaMailSender sender;
    
    @Value("${spring.mail.username}")
    private String from;

    public void sendEmail(List<String> toAddresses, String title, String content){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(toAddresses.toArray(new String[0]));
        message.setSubject(title);
        message.setText(content);
        sender.send(message);
    }
}