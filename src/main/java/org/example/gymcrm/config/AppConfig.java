package org.example.gymcrm.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static org.springframework.context.annotation.ComponentScan.*;

@Configuration
@EnableTransactionManagement
@EnableAspectJAutoProxy
@PropertySource("classpath:application.properties")
@ComponentScan(
    basePackages = "org.example.gymcrm",
    excludeFilters = {@Filter(Controller.class), @Filter(EnableWebMvc.class)})
public class AppConfig {
  @Bean
  public ObjectMapper objectMapper() {
    return new ObjectMapper();
  }
}
