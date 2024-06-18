package com.bnd.dailyband.controller;

import com.bnd.dailyband.domain.Member;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller

public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@RequestMapping("/main")
	public String main(Model model, HttpServletRequest request) {
		model.addAttribute("contextpath", request.getContextPath());

		return "dailyband/main";
	}

	@ModelAttribute
	public void addAttributes(Model model, @AuthenticationPrincipal Member member) {
		if (member != null) {
			model.addAttribute("profilePhoto", member.getProfilePhoto());
			model.addAttribute("username", member.getUsername());
		}
	}

}
