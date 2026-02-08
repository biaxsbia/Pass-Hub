package com.charlotte.passhub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// -Dspring.profiles.active=dev

@SpringBootApplication
public class passhubApplication {

	public static void main(String[] args) {
		SpringApplication.run(passhubApplication.class, args);
	}

}

