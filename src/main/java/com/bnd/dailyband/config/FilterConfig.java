package com.bnd.dailyband.config;

import com.bnd.dailyband.task.SessionFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {
  @Bean
  public FilterRegistrationBean<SessionFilter> loggingFilter(){
    FilterRegistrationBean<SessionFilter> registrationBean
        = new FilterRegistrationBean<>();

    registrationBean.setFilter(new SessionFilter());
    registrationBean.addUrlPatterns("/*");

    return registrationBean;
  }
}
