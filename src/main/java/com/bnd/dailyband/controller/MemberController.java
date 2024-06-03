package com.bnd.dailyband.controller;

import com.bnd.dailyband.domain.Ctgry;
import com.bnd.dailyband.domain.Member;
import com.bnd.dailyband.domain.MailVO;
import com.bnd.dailyband.service.s3upload.ImageUploadService;
import com.bnd.dailyband.task.SendMail;
import com.bnd.dailyband.domain.Social;
import com.bnd.dailyband.service.member.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;

@Controller
@RequestMapping("/member")
public class MemberController {

	private static final Logger logger = LoggerFactory.getLogger(MemberController.class);

	private MemberService memberService;
	private PasswordEncoder passwordEncoder;
	private SendMail sendMail;
	private ImageUploadService imageUploadService;

	@Autowired
	public MemberController(MemberService memberService,
							PasswordEncoder passwordEncoder,
							SendMail sendMail,ImageUploadService imageUploadService) {
		this.memberService=memberService;
		this.passwordEncoder=passwordEncoder;
		this.sendMail = sendMail;
		this.imageUploadService= imageUploadService;
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
	//회원가입폼에서 중복검사하는것
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

	//회원가입폼에서 이메일 검사
	@ResponseBody
	@RequestMapping(value = "/emailcheck", method = RequestMethod.GET)
	public int emailcheck(@RequestParam("email") String MBR_EML_ADDR){
		return memberService.isEmail(MBR_EML_ADDR);
	}

	//회원가입 폼에서 이메일전송
	@ResponseBody
	@RequestMapping(value = "/email", method = RequestMethod.GET)
	public String email(MailVO vo){
		sendMail.sendMail(vo);
		return vo.getContent();
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

	//회원가입의 결과 이동
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

	@PostMapping("/upload")
	public String upload(MultipartFile image, Model model, Principal userPrincipal) throws IOException {
		/* 이미지 업로드 로직 */
		String imageUrl = imageUploadService.upload(image);
		String id = userPrincipal.getName();

		memberService.imageupdate(imageUrl,id);
		Member member = memberService.myallinfo(id);

		UsernamePasswordAuthenticationToken authentication =
				new UsernamePasswordAuthenticationToken(member, member.getPassword(), member.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authentication);


		/* View에게 전달할 데이터를 Model에 담음 */
		model.addAttribute("imageUrl", imageUrl);



		/* View의 이름을 반환하여 View를 렌더링하도록 함 */
		return "redirect:update";
	}

	@GetMapping("/imageremove")
	public String imageremove(Model model, Principal userPrincipal) {
		/* 이미지 업로드 로직 */
		String id = userPrincipal.getName();
		String url = "https://myfirsttest1bucket.s3.ap-northeast-2.amazonaws.com/profile.png";
		memberService.imageupdate(url,id);

		Member member = memberService.myallinfo(id);

		UsernamePasswordAuthenticationToken authentication =
				new UsernamePasswordAuthenticationToken(member, member.getPassword(), member.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authentication);

		return "redirect:update";
	}

	@RequestMapping("/updateaction")
	public ModelAndView Modifyaction(Principal userPrincipal,
									 HttpServletRequest request, ModelAndView mv, RedirectAttributes redirect, Member member, Social social) {

		String id = userPrincipal.getName();
		String area[] = request.getParameterValues("MBR_PREFER_AREA");
		String genre[] = request.getParameterValues("MBR_PREFER_GENRE");
		String realm[] = request.getParameterValues("MBR_ACT_REALM");
		String tgenre = "";
		String trealm = "";
		String tarea = "";
		if (area != null && area.length != 0) {
			tarea += area[0];
			for (int i = 1; i < area.length; i++) {
				tarea += ","+area[i];
			}
		}
		if (genre != null && genre .length != 0) {
			tgenre  += genre [0];
			for (int i = 1; i < genre .length; i++) {
				tgenre += ","+genre [i];
			}
		}

		if (realm != null && realm .length != 0) {
			trealm  += realm [0];
			for (int i = 1; i < realm .length; i++) {
				trealm += ","+realm [i];
			}
		}

		member.setMBR_ACT_REALM(trealm);
		member.setMBR_PREFER_AREA(tarea);
		member.setMBR_PREFER_GENRE(tgenre);

		int modify = memberService.myinfo_modify(member,id);

		if (modify == 1) {
			redirect.addFlashAttribute("message", "정보 수정에 성공 했습니다.");
		}

		if (modify != 1) {
			redirect.addFlashAttribute("message", "정보 수정에 실패 했습니다.");
		}

		int ck = 0;
		String in = request.getParameter("INSTA_ADDR");
		String yt = request.getParameter("YT_ADDR");
		String sc = request.getParameter("SC_ADDR");
		String sp = request.getParameter("SPOTI_ADDR");

		if (in != null)
		{
			ck++;
		}

		if (yt != null)
		{
			ck++;
		}

		if (sc != null)
		{
			ck++;
		}

		if (sp != null)
		{
			ck++;
		}

		social.setINSTA_ADDR(in);
		social.setSC_ADDR(sc);
		social.setYT_ADDR(yt);
		social.setSPOTI_ADDR(sp);


		if (memberService.mysocial(id) != null && ck >= 1 ) { // 기존에 소셜 링크가 있으면서 소셜링크 입력한게 있다면
			memberService.social_update(social,id);
		}
		else if (memberService.mysocial(id) == null && ck >= 1) { // 기존에 소셜 링크가 없으면서 소셜 링크 입력한게 있다면
			memberService.social_insert(social,id);
		}
		mv.setViewName("redirect:update");
		return mv;
	}
}
