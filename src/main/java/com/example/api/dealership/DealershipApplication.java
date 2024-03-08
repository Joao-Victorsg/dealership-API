package com.example.api.dealership;


import co.elastic.apm.attach.ElasticApmAttacher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@Slf4j
@SpringBootApplication
@EnableFeignClients
public class DealershipApplication {

	public static void main(String[] args) {
		log.info("Iniciando aplicação");
		ElasticApmAttacher.attach();
		SpringApplication.run(DealershipApplication.class, args);
	}

}
