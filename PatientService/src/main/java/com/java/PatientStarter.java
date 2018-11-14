package com.java;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.java.dao")
@EntityScan(basePackages = "com.java.dto")
//@EnableEurekaClient
public class PatientStarter {

	public static void main(String[] args) {
		SpringApplication.run(PatientStarter.class, args);

	}

	@LoadBalanced
	@Bean
	RestTemplate getTemplate() {
		return new RestTemplate();
	}

}
