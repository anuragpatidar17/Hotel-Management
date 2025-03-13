package com.organisation.hotelManagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication(scanBasePackages = "com.organisation.hotelManagement")
@EnableJpaRepositories(basePackages = "com.organisation.hotelManagement.repository")
@EntityScan("com.organisation.hotelManagement.model")
@EnableScheduling
public class HotelBookingApplication {
	public static void main(String[] args) {
		SpringApplication.run(HotelBookingApplication.class, args);

	}


}
