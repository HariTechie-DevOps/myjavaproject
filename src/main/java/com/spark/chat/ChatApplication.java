package com.spark.chat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import java.util.Collections;

@SpringBootApplication
@ComponentScan(basePackages = {"com.spark.chat", "com.example.signup"})
@EnableJpaRepositories("com.spark.chat.repository") // Tell Spring where the Repo is
@EntityScan("com.spark.chat.entity")
public class ChatApplication {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(ChatApplication.class);
        
        // Setting JPA properties programmatically instead of application.properties
        app.setDefaultProperties(Collections.singletonMap("spring.jpa.hibernate.ddl-auto", "update"));
        app.setDefaultProperties(Collections.singletonMap("spring.jpa.show-sql", "true"));
        
        app.run(args);
    }
}
