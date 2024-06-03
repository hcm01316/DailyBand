package com.bnd.dailyband.task;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebFilter("/*")
public class SessionFilter implements Filter {

  private static final Logger logger = LoggerFactory.getLogger(SessionFilter.class);

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    Filter.super.init(filterConfig);
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response,
      FilterChain chain) throws IOException, ServletException {
    if (request instanceof HttpServletRequest) {
      HttpServletRequest httpRequest = (HttpServletRequest) request;

      //세션이 존재하지 않으면 새로운 세션 생성
      if (httpRequest.getSession(false) == null) {
        httpRequest.getSession(true);
        //logger.info("새로운 세션 생성 > 세션 ID: " + httpRequest.getSession().getId());
      } else {
        //logger.info("기존 세션 존재 > 세션 ID: " + httpRequest.getSession().getId());
      }
    }
    chain.doFilter(request, response);
  }

  @Override
  public void destroy() {
    Filter.super.destroy();
  }
}
