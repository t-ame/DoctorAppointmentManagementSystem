package com.java;

import javax.jms.ConnectionFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableJpaRepositories(basePackages="com.java.dao")
@EnableTransactionManagement(proxyTargetClass=false)
@EntityScan(basePackages="com.java.dto")
public class MyStarter {

	@Autowired ConnectionFactory factory;
	public static void main(String[] args) {
		SpringApplication.run(MyStarter.class, args);
//		Appointment appointment= Appointment.appointment().doctorName("kapil").clinicAddress("").build();
	}
	
	@Bean
	public JmsTemplate template() {
		JmsTemplate template= new JmsTemplate(factory);
		template.setMessageConverter(new MappingJackson2MessageConverter());
		return template;
	}

}
