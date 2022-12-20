package com.sparta.spartaposthomework;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SpartaPostHomeworkApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpartaPostHomeworkApplication.class, args);
	}

}