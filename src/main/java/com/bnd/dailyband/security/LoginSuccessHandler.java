package com.bnd.dailyband.security;

import java.io.IOException;

import jakarta.servlet.http.Cookie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {
	private static final Logger logger = LoggerFactory.getLogger(LoginSuccessHandler.class); 
		
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException{
		logger.info("로그인 성공 : LoginSucessHandler ");

		Cookie savecookie = new Cookie("saveid", authentication.getName());

		String remember= request.getParameter("remember-me");

		if(remember != null && remember.equals("store")) {
			savecookie.setMaxAge(60*60*24); //1일
			logger.info("쿠키저장 : 60*60*24");
		}else {
			logger.info("쿠키저장 : 0");
			savecookie.setMaxAge(0);
		}
		response.addCookie(savecookie);

		String url=request.getContextPath()+"/main";
		response.sendRedirect(url);

	}


}
