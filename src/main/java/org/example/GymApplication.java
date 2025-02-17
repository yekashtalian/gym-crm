package org.example;

import org.example.gymcrm.config.AppConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class GymApplication {
  public static void main(String[] args) {
    var context = new AnnotationConfigApplicationContext(AppConfig.class);
  }
}
