package com.bnd.dailyband.controller;

import com.bnd.dailyband.domain.*;
import com.bnd.dailyband.service.rboard.RboardService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/rboard")
public class RboardController {

	private static final Logger logger = LoggerFactory.getLogger(RboardController.class);

	private RboardService rboardService;


	@Autowired
	public RboardController(RboardService rboardService)
	{
		this.rboardService = rboardService;
	}

	@ModelAttribute
	public void addAttributes(Model model, @AuthenticationPrincipal Member member) {
		if (member != null) {
			model.addAttribute("profilePhoto", member.getProfilePhoto());
			model.addAttribute("username", member.getUsername());
		}
	}


	@GetMapping("/detail")
	public ModelAndView Detail(
			int num, ModelAndView mv,
			HttpServletRequest request, @RequestHeader(value ="referer", required= false)String beforeURL) {

		logger.info("regerer:" + beforeURL);
		if(beforeURL!=null && beforeURL.endsWith("list")) {
			rboardService.setReadCountUpdate(num);
		}

		Rboard rboard = rboardService.getDetail(num);

		int cnt = rboardService.bandacceptcnt(num);
		// board= null; //error 페이지 이동 확인 하고자 임의로 저장합니다.
		if (rboard == null) {
			logger.info("상세보기 실패");
			mv.setViewName("error/error");
			mv.addObject("url",request.getRequestURL());
			mv.addObject("message", "상세보기 실패입니다.");
		}
		else {
			logger.info("상세보기 성공");
			mv.setViewName("rboard/rboard_view");
			mv.addObject("current","rBoard");
			mv.addObject("rboarddata", rboard);
			mv.addObject("cnt", cnt);
		}
		return mv;
	}


	@RequestMapping("/write")
	public ModelAndView rboardwrite(ModelAndView mv) {

		ArrayList<Ctgry> Arealist = rboardService.getCtgryList(0);
		ArrayList<Ctgry> Genrelist = rboardService.getCtgryList(1);
		ArrayList<Ctgry> Realemlist = rboardService.getCtgryList(2);
		mv.addObject("Arealist", Arealist);
		mv.addObject("Genrelist", Genrelist);
		mv.addObject("Realemlist", Realemlist);
		mv.addObject("current","rBoard");
		mv.setViewName("rboard/rboard_write");
		return mv;
	}

	@RequestMapping("/add")
	public String add(Rboard rboard, HttpServletRequest request)
	{
		String realm[] = request.getParameterValues("realem");
		String trealm = "";
		if (realm.length != 0) {
			trealm += realm[0];
			for (int i = 1; i < realm.length; i++) {
				trealm += ","+realm[i];
			}
		}

		String id = rboard.getMBR_ID();

		rboard.setRCRIT_REALM_ID(trealm);
		rboardService.insertRboard(rboard);

		int num = rboardService.getaddnum();
		rboardService.insertBand(id,num);

		logger.info(rboard.toString()); // selectKey로 정의한 BOARD_NUM 값 확인해 봅시다.
		return "redirect:list";
	}


	@RequestMapping ("/bandHR")
	public ModelAndView mgmtlist(ModelAndView mv, HttpServletRequest request,Principal userPrincipal) {

		String id = userPrincipal.getName();
		mv.addObject("id", id);

		int bandck = rboardService.bandck(id);
		mv.addObject("bandck", bandck);
		mv.addObject("current","bandHR");

		if (bandck == -1) {
			mv.addObject("isband", 0);
		}
		if (bandck != -1)
		{
			int leaderck = rboardService.leaderck(id);
			mv.addObject("leaderck",leaderck);
			List<Bandhr> bandlist = new ArrayList<Bandhr>();
			int myband = rboardService.myband(id);
			bandlist = rboardService.getbandList(myband);
			mv.addObject("bandlist", bandlist);
			List<Bandhr> joinlist = new ArrayList<Bandhr>();
			joinlist = rboardService.getjoinlist(myband);
			mv.addObject("joinlist", joinlist);
			logger.info(bandlist.toString());
		}
		mv.setViewName("rboard/rboard_mgmt");
		return mv;
	}


	@RequestMapping(value= "/list", method=RequestMethod.GET)
	public ModelAndView boardList(@RequestParam(value="page",defaultValue="1") int page, ModelAndView mv,Principal userPrincipal) {

		String id = userPrincipal.getName();
		int limit = 4; // 한 화면에 출력할 로우 갯수

		int listcount = rboardService.getListCount(); // 총 리스트 수를 받아옴

		//총 페이지 수
		int maxpage = (listcount + limit - 1) / limit;

		//현재 페이지에 보여줄 시작 페이지 수 (1, 11, 21 등...)
		int startpage = ((page - 1) / 10) * 10 + 1;

		int endpage = startpage + 10 - 1;
		if (endpage > maxpage)
			endpage = maxpage;

		List<Rlist> rboardlist = rboardService.getRboardList(page, limit); // 리스트를 받아옴
		int bandck = rboardService.bandck(id);
		if (bandck == -1) {
			mv.addObject("isband", 0);
		}
		else  {
			mv.addObject("isband", 1);
		}

		mv.setViewName("rboard/rboard_list");
		mv.addObject("page",page);
		mv.addObject("maxpage", maxpage);
		mv.addObject("startpage", startpage);
		mv.addObject("endpage", endpage);
		mv.addObject("listcount", listcount);
		mv.addObject("rboardlist", rboardlist);
		mv.addObject("limit", limit);
		mv.addObject("current", "rBoard");
		return mv;
	}

	@RequestMapping ("/join")
	public ModelAndView join(ModelAndView mv, HttpServletRequest request,int num,Principal userPrincipal, RedirectAttributes redirect) {

		String id = userPrincipal.getName();
		int isjoin = rboardService.isjoin(id);

		if (isjoin == 0) {
			rboardService.join(id,num);
			redirect.addFlashAttribute("message", "가입 신청이 성공적으로 완료되었습니다.");
		}

		if (isjoin >= 1) {
			redirect.addFlashAttribute("message", "이미 신청을 했거나 밴드에 참여중 입니다.");
		}
		mv.addObject("num", num);
		mv.setViewName("redirect:detail");
		return mv;
	}

	@RequestMapping ("/resign")
	public ModelAndView resign(ModelAndView mv, HttpServletRequest request,String id, int num, RedirectAttributes redirect) {

		int resign = rboardService.resign(id,num);

		int nope = rboardService.getrenope(num);

		if (nope == 0)
		{
			rboardService.teamstopen(num);
		}

		if (resign == 0) {
			redirect.addFlashAttribute("message", "강퇴에 실패 했습니다.");
		}

		if (resign == 1) {
			redirect.addFlashAttribute("message", "성공적으로 강퇴 하였습니다.");
		}
		mv.setViewName("redirect:bandHR");
		return mv;
	}

	@RequestMapping ("/accept")
	public ModelAndView accept(ModelAndView mv, HttpServletRequest request,String id, int num, RedirectAttributes redirect) {

		int accept = rboardService.bandaccept(id,num);

		if (accept == 0) {
			redirect.addFlashAttribute("message", "수락에 실패했습니다.");
		}

		if (accept == 1) {
			redirect.addFlashAttribute("message", "성공적으로 수락 하였습니다.");
			rboardService.joinwatingdel(id);

			int cnt = rboardService.bandacceptcnt(num);
			int nope = rboardService.getrenope(num);

			if(cnt == nope)
			{
				rboardService.teamstclose(num);
			}
		}
		mv.setViewName("redirect:bandHR");
		return mv;
	}

	@RequestMapping ("/refuse")
	public ModelAndView refuse(ModelAndView mv, HttpServletRequest request,String id, int num, RedirectAttributes redirect) {



		int refuse = rboardService.refuse(id,num);

		if (refuse == 0) {
			redirect.addFlashAttribute("message", "거절에 실패했습니다.");
		}

		if (refuse == 1) {
			redirect.addFlashAttribute("message", "성공적으로 거절 하였습니다.");
		}
		mv.setViewName("redirect:bandHR");
		return mv;
	}

	@RequestMapping ("/breakup")
	public ModelAndView breakup(ModelAndView mv, HttpServletRequest request,RedirectAttributes redirect,Principal userPrincipal) {

		String id = userPrincipal.getName();

		int myband = rboardService.myband(id);
		int breakup = rboardService.breakup(myband);

		if (breakup == 0) {
			redirect.addFlashAttribute("message", "해체에 실패했습니다.");
		}

		if (breakup == 1) {
			redirect.addFlashAttribute("message", "성공적으로 해체 하였습니다.");
		}
		mv.setViewName("redirect:bandHR");
		return mv;
	}

	@RequestMapping ("/modify")
	public ModelAndView breakup(ModelAndView mv, HttpServletRequest request,int num,Principal userPrincipal) {

		Rboard rboard = rboardService.getDetail(num);
		String id = userPrincipal.getName();

		ArrayList<Ctgry> Arealist = rboardService.getCtgryList(0);
		ArrayList<Ctgry> Genrelist = rboardService.getCtgryList(1);
		ArrayList<Ctgry> Realemlist = rboardService.getCtgryList(2);
		mv.addObject("Arealist", Arealist);
		mv.addObject("Genrelist", Genrelist);
		mv.addObject("Realemlist", Realemlist);
		mv.addObject("writer",id);
		mv.addObject("rboarddata", rboard);
		mv.addObject("current","bandHR");
		mv.setViewName("rboard/rboard_modify");
		return mv;
	}

	@RequestMapping ("/delete")
	public ModelAndView delete(ModelAndView mv, HttpServletRequest request,int num,RedirectAttributes redirect) {

		int delete = rboardService.breakup(num);

		if (delete == 0) {
			redirect.addFlashAttribute("message", "게시글 삭제에 실패 했습니다.");
		}

		if (delete == 1) {
			redirect.addFlashAttribute("message", "게시글 삭제에 성공 하였습니다.");
		}
		mv.setViewName("redirect:list");
		return mv;
	}

	@RequestMapping ("/leave")
	public ModelAndView leave(ModelAndView mv, HttpServletRequest request,int num,RedirectAttributes redirect,Principal userPrincipal) {

		String id = userPrincipal.getName();
		int leave = rboardService.leave(id,num);

		int nope = rboardService.getrenope(num);

		if (nope == 0)
		{
			rboardService.teamstopen(num);
		}

		if (leave == 0) {
			redirect.addFlashAttribute("message", "성공적으로 탈퇴 했습니다.");
		}

		if (leave  == 1) {
			redirect.addFlashAttribute("message", "탈퇴에 실패 했습니다.");
		}
		mv.setViewName("redirect:list");
		return mv;
	}

	@RequestMapping("/modifyaction")
	public String modifyaction(Rboard rboard, HttpServletRequest request)
	{
		String realm[] = request.getParameterValues("realem");
		String trealm = "";
		if (realm.length != 0) {
			trealm += realm[0];
			for (int i = 1; i < realm.length; i++) {
				trealm += ","+realm[i];
			}
		}

		rboard.setRCRIT_REALM_ID(trealm);
		rboardService.updateRboard(rboard);

		logger.info(rboard.toString()); // selectKey로 정의한 BOARD_NUM 값 확인해 봅시다.
		return "redirect:list";
	}


}
