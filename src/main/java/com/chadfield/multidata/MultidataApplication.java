package com.chadfield.multidata;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class MultidataApplication {

	public static void main(String[] args) {
		SpringApplication.run(MultidataApplication.class, args);
	}

}
