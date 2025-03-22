package org.example.gymcrm.config;

import lombok.RequiredArgsConstructor;
import org.example.gymcrm.dao.UserDao;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Configuration
@RequiredArgsConstructor
public class UserDetailsConfig {
  private final UserDao userDao;

  @Bean
  public UserDetailsService userDetailsService() {
    return username ->
        userDao
            .findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
  }
}
