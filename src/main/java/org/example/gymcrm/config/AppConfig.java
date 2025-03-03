package org.example.gymcrm.config;

import static org.springframework.context.annotation.ComponentScan.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

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
