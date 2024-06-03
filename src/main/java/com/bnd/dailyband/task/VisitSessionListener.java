package com.bnd.dailyband.task;


import com.bnd.dailyband.service.admin.AdminService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VisitSessionListener implements HttpSessionListener {

  private static final Logger logger = LoggerFactory.getLogger(VisitSessionListener.class);

  @Autowired
  private AdminService adminService;

  @Autowired
  private HttpServletRequest request;

  @Override
  public void sessionCreated(HttpSessionEvent se) {
    logger.info("생성된 세션 ID: " + se.getSession().getId());
    adminService.addVisit();
  }

  @Override
  public void sessionDestroyed(HttpSessionEvent se) {

  }
}
