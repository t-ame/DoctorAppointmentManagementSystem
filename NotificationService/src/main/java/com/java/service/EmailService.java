package com.java.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.java.util.Email;

@Service
public class EmailService {

	@Autowired
	JavaMailSender mailUtil;

	public void sendEmail(Email email) {
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo(email.getTo());
		mailMessage.setSubject(email.getSubject());
		mailMessage.setText(email.getContent());
		mailUtil.send(mailMessage);
	}

//	public void sendEmailWithAttachment(String to,String subject,String body,String filePath) throws MessagingException {
//		MimeMessage mailMessage=mailUtil.createMimeMessage();
//		MimeMessageHelper messageHelper=new  MimeMessageHelper(mailMessage,true);
//		messageHelper.setTo(to);
//		messageHelper.setSubject(subject);
//		messageHelper.setText(body);
//		FileSystemResource fileSystemResource=new FileSystemResource(filePath);
//		messageHelper.addAttachment(new File(filePath).getName(), fileSystemResource);
//		mailUtil.send(mailMessage);
//	}
}
