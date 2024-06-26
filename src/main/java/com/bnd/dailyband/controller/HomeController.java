package com.bnd.dailyband.controller;

import com.bnd.dailyband.domain.Member;
import com.bnd.dailyband.service.admin.AdminService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller

public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

	private AdminService adminService;

	@Autowired
	public HomeController(AdminService adminService) {
		this.adminService = adminService;
	}

	@RequestMapping("/main")
	public String main(Model model, HttpServletRequest request) {
		model.addAttribute("contextpath", request.getContextPath());


		return "dailyband/main";
	}

	@ModelAttribute
	public void addAttributes(Model model, HttpServletRequest request, @AuthenticationPrincipal Member member) {
		model.addAttribute("contextpath", request.getContextPath());

		int resCnt = adminService.resWaitCnt();
		model.addAttribute("resCnt", resCnt);

		if (member != null) {
			model.addAttribute("profilePhoto", member.getProfilePhoto());
			model.addAttribute("username", member.getUsername());
		}
	}



}
