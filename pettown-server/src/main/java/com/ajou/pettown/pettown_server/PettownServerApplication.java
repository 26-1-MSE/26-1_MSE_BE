package com.ajou.pettown.pettown_server;

// Entry point of the PetTown Spring Boot application.
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = "com.ajou.pettown")
@EnableJpaRepositories(basePackages = "com.ajou.pettown")
@EntityScan(basePackages = "com.ajou.pettown")
public class PettownServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(PettownServerApplication.class, args);
    }
}
