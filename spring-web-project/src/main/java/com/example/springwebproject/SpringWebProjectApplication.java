package com.example.springwebproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SpringWebProjectApplication {

    public static void main(String[]  args) {
        SpringApplication.run(SpringWebProjectApplication.class, args);
    }

}
