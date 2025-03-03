package org.example.gymcrm.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.example.gymcrm.logging.interceptor.RequestLoggingInterceptor;
import org.example.gymcrm.logging.interceptor.TransactionLoggingInterceptor;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
@EnableAspectJAutoProxy
@ComponentScan(
    basePackages = {"org.example.gymcrm.web", "org.example.gymcrm.logging", "org.springdoc"})
@Import({
  org.springdoc.core.configuration.SpringDocConfiguration.class,
  org.springdoc.webmvc.core.configuration.SpringDocWebMvcConfiguration.class,
  org.springdoc.webmvc.ui.SwaggerConfig.class,
  org.springdoc.core.properties.SwaggerUiConfigProperties.class,
  org.springdoc.core.properties.SwaggerUiOAuthProperties.class,
  org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration.class
})
public class WebConfig implements WebMvcConfigurer {
  @Bean
  public GroupedOpenApi publicApi() {
    return GroupedOpenApi.builder().group("public").pathsToMatch("/api/v1/**").build();
  }

  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI()
        .info(
            new Info()
                .title("Gym CRM API")
                .version("1.0")
                .description("API documentation for Gym CRM system"));
  }
  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(new TransactionLoggingInterceptor());
    registry.addInterceptor(new RequestLoggingInterceptor());
  }
}
