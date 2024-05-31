package com.bnd.dailyband.controller;

import com.bnd.dailyband.domain.Ctgry;
import com.bnd.dailyband.domain.Member;
import com.bnd.dailyband.domain.Social;
import com.bnd.dailyband.service.member.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.ArrayList;

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

	@ModelAttribute
	public void addAttributes(Model model, @AuthenticationPrincipal Member member) {
		if (member != null) {
			model.addAttribute("profilePhoto", member.getProfilePhoto());
			model.addAttribute("username", member.getUsername());
		}
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

	//회원가입 폼으로 이동
	@RequestMapping(value = "/join", method = RequestMethod.GET)
	public String join(Model model) {
		return "member/join";
	}

	//회원가입폼에서 아이디 검사
	@ResponseBody
	@RequestMapping(value = "/idcheck", method = RequestMethod.GET)
	public int idcheck(@RequestParam("id") String MBR_ID){
		return memberService.isId(MBR_ID);
	}

	//회원가입폼에서 닉네임 검사
	@ResponseBody
	@RequestMapping(value = "/namecheck", method = RequestMethod.GET)
	public int idname(@RequestParam("name") String MBR_NCNM){
		return memberService.isName(MBR_NCNM);
	}

	//회원가입 처리
	@RequestMapping(value = "/joinProcess", method = RequestMethod.POST)
	public String joinProcess( Member member,
							   RedirectAttributes rattr,
							   Model model,
							   HttpServletRequest request) {

		//비밀번호 암호화
		String encPassword = passwordEncoder.encode(member.getMBR_PWD());
		logger.info(encPassword);
		member.setMBR_PWD(encPassword);

		int result = memberService.insert(member);

		// 내용 삽입이 된 경우
		if (result == 1) {
			rattr.addFlashAttribute("result", "joinSuccess");
			return "redirect:join2";
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


	//회원가입2
	@RequestMapping(value = "/join2", method = RequestMethod.GET)
	public String join2( Member member)
	{
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

	@RequestMapping("/update")
	public ModelAndView infoModify(Principal userPrincipal,
								HttpServletRequest request, ModelAndView mv) {
		String id = userPrincipal.getName();
		Member m = memberService.member_info(id);

		Social mysocial = memberService.mysocial(id);

			mv.addObject("mysocial", mysocial);


		ArrayList<Ctgry> Arealist = memberService.getCtgryList(0);
		ArrayList<Ctgry> Genrelist = memberService.getCtgryList(1);
		ArrayList<Ctgry> Realemlist = memberService.getCtgryList(2);
		mv.addObject("Arealist", Arealist);
		mv.addObject("Genrelist", Genrelist);
		mv.addObject("Realemlist", Realemlist);

		logger.info("회원 아이디 : " + id);
		if(m != null) {
			mv.setViewName("member/info_modify");
			mv.addObject("memberinfo", m);
			mv.addObject("mysocial", mysocial);
			logger.info("멤버 정보 : " + m.getMBR_PROFL_PHOTO());
		} else {
			mv.addObject("url", request.getRequestURL());
			mv.addObject("message", "해당 정보가 없습니다.");
			mv.setViewName("error/error");
		}
		return mv;
	}

	@RequestMapping("/updateaction")
	public ModelAndView Modifyaction(Principal userPrincipal,
								   HttpServletRequest request, ModelAndView mv,RedirectAttributes redirect) {
		String id = userPrincipal.getName();
		int modify = memberService.myinfo_modify(id);
		Social social = new Social();


		if (social != null) {
			//Social social = memberService.mysocial(id);
			redirect.addFlashAttribute("message", "정보수정이 성공적으로 완료되었습니다.");
		}

		else {
			redirect.addFlashAttribute("message", "정보수정이 실패했습니다.");
		}
		mv.setViewName("redirect:update");
		return mv;
	}
}
