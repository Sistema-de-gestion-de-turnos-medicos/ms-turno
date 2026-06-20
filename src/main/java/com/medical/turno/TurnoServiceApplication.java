package com.medical.turno;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class TurnoServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TurnoServiceApplication.class, args);
    }
}
