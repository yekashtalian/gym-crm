package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
public class GymApplication {
  public static void main(String[] args) {
    SpringApplication.run(GymApplication.class, args);
  }
}
