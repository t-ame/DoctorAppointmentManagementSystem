package com.java;

import javax.jms.ConnectionFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import brave.sampler.Sampler;

@SpringBootApplication
@EnableJms
@EnableJpaRepositories(basePackages = "com.java.dao")
@EnableTransactionManagement(proxyTargetClass = false)
@EntityScan(basePackages = "com.java.dto")
public class AppointmentStarter {

	@Autowired
	ConnectionFactory factory;

	public static void main(String[] args) {
		SpringApplication.run(AppointmentStarter.class, args);
	}

	@Bean
	public JmsTemplate template() {
		JmsTemplate template = new JmsTemplate(factory);
		template.setMessageConverter(new MappingJackson2MessageConverter());
		return template;
	}
	
	@Bean
	Sampler getSampler() {
		return Sampler.ALWAYS_SAMPLE;
	}

}
