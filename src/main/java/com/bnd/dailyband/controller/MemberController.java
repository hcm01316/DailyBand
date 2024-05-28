package com.bnd.dailyband.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpSession;
import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bnd.dailyband.domain.Member;
import com.bnd.dailyband.service.member.MemberService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/member")
public class MemberController {

	private static final Logger logger = LoggerFactory.getLogger(MemberController.class);

	private MemberService memberService;
	private PasswordEncoder passwordEncoder;

	@Autowired
	public MemberController(MemberService memberService,
							PasswordEncoder passwordEncoder
	) {
		this.memberService=memberService;
		this.passwordEncoder=passwordEncoder;
	}

	//로그인 폼으로 이동
	@RequestMapping(value ="/login", method = RequestMethod.GET)
	public ModelAndView login(ModelAndView mv,
							  @CookieValue(value="saveid", required=false) Cookie readCookie,
							  HttpSession session,
							  Principal userPrincipal
	) {
		if(readCookie != null) {
			// principal.getName() : 로그인한 아이디 값을 알 수 있어요
			mv.addObject("saveid", readCookie.getValue());
		}

		if(session.getAttribute("loginfail") != null) {
			// 세션에 저장된 값ㅇ르 한 번만 실행될 수 있도록 model에 저장
			mv.addObject("loginfail", session.getAttribute("loginfail"));

			session.removeAttribute("loginfail"); // 세션의 값은 제거합니다.
		}
		mv.setViewName("member/login");
		return mv;
	}

	//회원가입 촘으로 이동
	@RequestMapping(value = "/join", method = RequestMethod.GET)
	public String join(Model model) {
		return "member/join";
	}

	@RequestMapping(value = "/joinProcess", method = RequestMethod.POST)
	public String joinProcess(	Member member,
								  RedirectAttributes rattr,
								  Model model,
								  HttpServletRequest request) {
		String encPassword = passwordEncoder.encode(member.getMBR_PWD());
		logger.info(encPassword);
		member.setMBR_PWD(encPassword);
		int result = memberService.insert(member);

		if (result == 1) {
			rattr.addFlashAttribute("result", "joinSuccess");
			return "redirect:login";
		}else {
			model.addAttribute("url", request.getRequestURL());
			model.addAttribute("message", "회원 가입 실패");
			return "error/error";
		}
	}

	//로그아웃
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:login";
	}


	@RequestMapping("/join2")
	public String join2(Model model) {
		return "member/join2";
	}

	@RequestMapping("/myprofile")
	public ModelAndView myProfile(
			HttpServletRequest request,
			ModelAndView mv) {
		Principal principal = request.getUserPrincipal();
		Member m = memberService.member_info(principal.getName());
		//logger.info("회원 principal.getName = " + principal.getName());
		mv.setViewName("member/my_profile");
		mv.addObject("memberinfo", m);
		return mv;
	}

	@RequestMapping("/info")
	public ModelAndView mbrInfo(@RequestParam("id") String id,
			HttpServletRequest request, ModelAndView mv) {
		Member m = memberService.member_info(id);

		logger.info("회원 아이디 : " + id);
		if(m != null) {
			mv.setViewName("member/member_info");
			mv.addObject("memberinfo", m);
		} else {
			mv.addObject("url", request.getRequestURL());
			mv.addObject("message", "해당 정보가 없습니다.");
			mv.setViewName("error/error");
		}
		return mv;
	}

	//아이디와 비밀번호 찾기
	@RequestMapping("/forgot")
	public String forgot(Model model) {
		return "member/forgot";
	}

}
