package com.example.api.dealership;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@SpringBootApplication
public class DealershipApplication {

	public static void main(String[] args) {
		log.info("Iniciando aplicação");
		SpringApplication.run(DealershipApplication.class, args);
	}

}
