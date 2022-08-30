package com.example.api.dealership;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@Slf4j
@SpringBootApplication
public class DealershipApplication {

	public static void main(String[] args) {
		log.info("Iniciando aplicação");
		SpringApplication.run(DealershipApplication.class, args);
	}

}
