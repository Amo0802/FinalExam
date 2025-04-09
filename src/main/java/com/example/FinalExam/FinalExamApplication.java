package com.example.FinalExam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
@EnableCaching
public class FinalExamApplication {

	private static final Logger logger = LoggerFactory.getLogger(FinalExamApplication.class);

	public static void main(String[] args) {
		logger.info("Starting FinalExam Application");
		SpringApplication.run(FinalExamApplication.class, args);
		logger.info("FinalExam Application started successfully");
	}
}
