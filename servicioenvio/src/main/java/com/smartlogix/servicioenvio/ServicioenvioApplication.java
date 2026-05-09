package com.smartlogix.servicioenvio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class ServicioenvioApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServicioenvioApplication.class, args);
	}

}
