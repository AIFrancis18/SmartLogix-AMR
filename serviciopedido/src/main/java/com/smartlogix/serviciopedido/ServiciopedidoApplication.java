package com.smartlogix.serviciopedido;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(basePackages = "com.smartlogix.serviciopedido.client") 
@SpringBootApplication
public class ServiciopedidoApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiciopedidoApplication.class, args);
    }
}