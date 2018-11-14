package com.java;

import java.util.Properties;

import javax.jms.ConnectionFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableJms
@EnableJpaRepositories(basePackages = "com.java.dao")
@EnableTransactionManagement(proxyTargetClass = false)
@EntityScan(basePackages = "com.java.dto")
public class NotificationStarter {

	@Autowired
	ConnectionFactory factory;

	public static void main(String[] args) {
		SpringApplication.run(NotificationStarter.class, args);
	}

	@Bean
	public JmsTemplate template() {
		JmsTemplate template = new JmsTemplate(factory);
		template.setMessageConverter(new MappingJackson2MessageConverter());
		return template;
	}

	@Bean
	DefaultJmsListenerContainerFactory getbean() {
		return new DefaultJmsListenerContainerFactory();
	}

	@Bean("mailUtil")
	public JavaMailSender getJavaMailSender() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost("smtp.gmail.com");
		mailSender.setPort(587);

		mailSender.setUsername("toya.amechi@gmail.com");
		mailSender.setPassword("Special.girl1");

		Properties props = mailSender.getJavaMailProperties();
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.debug", "true");

		return mailSender;
	}

}
