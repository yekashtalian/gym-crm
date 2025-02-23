package org.example.gymcrm.logging.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;

public class TransactionLoggingInterceptor implements HandlerInterceptor {
  private static final String TRANSACTION_ID = "transactionId";

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
    var transactionId = UUID.randomUUID().toString();
    MDC.put(TRANSACTION_ID, transactionId);
    request.setAttribute(TRANSACTION_ID, transactionId);
    return true;
  }

  @Override
  public void afterCompletion(
      HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
      throws Exception {
    MDC.clear();
  }
}
