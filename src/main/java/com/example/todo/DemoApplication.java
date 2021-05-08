package com.example.todo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {
	public static void main(String[] args) {

		// AbstractApplicationContext context = new AnnotationConfigApplicationContext(JPAConfiguration.class);
		SpringApplication.run(DemoApplication.class, args);
	}

}
