package org.example.gymcrm.logging.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Collections;
import java.util.Enumeration;
import java.util.stream.Collectors;

public class RequestLoggingInterceptor implements HandlerInterceptor {
  private static final Logger logger = LoggerFactory.getLogger(RequestLoggingInterceptor.class);

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
    var headers = getHeadersAsString(request);
    logger.info(
        "Incoming request: {} {} | Headers {}",
        request.getMethod(),
        request.getRequestURI(),
        headers);
    return true;
  }

  @Override
  public void afterCompletion(
      HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
      throws Exception {
    logger.info("Response status: {}", response.getStatus());

    if (ex != null) {
      logger.warn("Error occurred: {}", ex.getMessage(), ex);
    }
  }

  private String getHeadersAsString(HttpServletRequest request) {
    Enumeration<String> headerNames = request.getHeaderNames();
    return headerNames != null
        ? Collections.list(headerNames).stream()
            .map(name -> name + "=" + request.getHeader(name))
            .collect(Collectors.joining(", "))
        : "No headers";
  }
}
