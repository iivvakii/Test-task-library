package org.example.testtasknerdy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class TestTaskNerdyApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestTaskNerdyApplication.class, args);
    }

}
