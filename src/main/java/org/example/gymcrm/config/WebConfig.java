package org.example.gymcrm.config;

import org.example.gymcrm.logging.interceptor.RequestLoggingInterceptor;
import org.example.gymcrm.logging.interceptor.TransactionLoggingInterceptor;
import org.springframework.context.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
@EnableAspectJAutoProxy
@ComponentScan(basePackages = {"org.example.gymcrm.web", "org.example.gymcrm.logging"})
public class WebConfig implements WebMvcConfigurer {
  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(new TransactionLoggingInterceptor());
    registry.addInterceptor(new RequestLoggingInterceptor());
  }
}
