package com.bnd.dailyband.controller;

import com.bnd.dailyband.domain.Ctgry;
import com.bnd.dailyband.domain.Mgmt;
import com.bnd.dailyband.domain.Rboard;
import com.bnd.dailyband.domain.Rlist;
import com.bnd.dailyband.service.rboard.RboardService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

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


	@GetMapping("/detail")
	public ModelAndView Detail(
			int num, ModelAndView mv,
			HttpServletRequest request, @RequestHeader(value ="referer", required= false)String beforeURL) {

		/*
		 * 1. String beforeURL = request.getHeader("referer"); 의미로
		 * 	어느 주소에서 detail로 이동했는지 header의 정보 중에서 "referer"를 통해 알 수 있습니다.
		 * 2. 수정 후 이곳으로 이동하는 경우 조회수는 증가하지 않도록 합니다.
		 * 3. myhome4/board/list에서 제목을 클릭한 경우 조회수가 증가하도록 합니다.
		 *
		 *
		 */
		logger.info("regerer:" + beforeURL);
		if(beforeURL!=null && beforeURL.endsWith("list")) {
			rboardService.setReadCountUpdate(num);
		}

		Rboard rboard = rboardService.getDetail(num);
		// board= null; //error 페이지 이동 확인 하고자 임의로 저장합니다.
		if (rboard == null) {
			logger.info("상세보기 실패");
			mv.setViewName("error/error");
			mv.addObject("url",request.getRequestURL());
			mv.addObject("message", "상세보기 실패입니다.");
		}
		else {
			logger.info("상세보기 성공");
			mv.setViewName("rboard_view");
			mv.addObject("rboarddata", rboard);
		}
		return mv;
	}

	@RequestMapping("/list")
	public String rboardlist(Model model) {
		return "rboardList";
	}

	@RequestMapping("/write")
	public ModelAndView rboardwrite(ModelAndView mv) {

		ArrayList<Ctgry> Arealist = rboardService.getCtgryList(0);
		ArrayList<Ctgry> Genrelist = rboardService.getCtgryList(1);
		ArrayList<Ctgry> Realemlist = rboardService.getCtgryList(2);
		mv.addObject("Arealist", Arealist);
		mv.addObject("Genrelist", Genrelist);
		mv.addObject("Realemlist", Realemlist);
		mv.setViewName("rboard_write");
		return mv;
	}

	@PostMapping("/add")
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
        rboard.setRCRIT_REALM_ID(trealm);
		rboardService.insertRboard(rboard); // 저장 메서드 호출
		logger.info(rboard.toString()); // selectKey로 정의한 BOARD_NUM 값 확인해 봅시다.
		return "redirect:write";
	}


	@RequestMapping ("/bandHR")
	public ModelAndView mgmtlist(ModelAndView mv, HttpServletRequest request,Principal userPrincipal) {

		String id = userPrincipal.getName();
		mv.addObject("id", id);
		int bandck = rboardService.bandck(id);
		mv.addObject("bandck", bandck);
		mv.addObject("current","bandHR");

		if (bandck != -1)
		{
			int leaderck = rboardService.leaderck(id);
			mv.addObject("leaderck",leaderck);
			List<Mgmt> bandlist = new ArrayList<Mgmt>();
			bandlist = rboardService.getbandList(bandck);
			mv.addObject("bandlist", bandlist);
			logger.info(bandlist.toString());
		}


		//List<Rboard> joinlist = new ArrayList<Rboard>();
		//List<Rboard> bandlist = new ArrayList<Rboard>();
		//rboardService
		mv.setViewName("rboard/rboard_mgmt");
		return mv;
	}

	@RequestMapping ("/accept")
	public ModelAndView accept(ModelAndView mv, HttpServletRequest request, String id) {

		rboardService.bandaccept(id);
		mv.setViewName("rboard/rboard_mgmt");

		return mv;
	}

	@RequestMapping(value= "/list", method=RequestMethod.GET)
	public ModelAndView boardList(@RequestParam(value="page",defaultValue="1") int page, ModelAndView mv) {

		int limit = 10; // 한 화면에 출력할 로우 갯수

		int listcount = rboardService.getListCount(); // 총 리스트 수를 받아옴

		//총 페이지 수
		int maxpage = (listcount + limit - 1) / limit;

		//현재 페이지에 보여줄 시작 페이지 수 (1, 11, 21 등...)
		int startpage = ((page - 1) / 10) * 10 + 1;

		int endpage = startpage + 10 - 1;
		if (endpage > maxpage)
			endpage = maxpage;

		List<Rlist> rboardlist = rboardService.getRboardList(page, limit); // 리스트를 받아옴

		mv.setViewName("rboard/rboard_list");
		mv.addObject("page",page);
		mv.addObject("maxpage", maxpage);
		mv.addObject("startpage", startpage);
		mv.addObject("endpage", endpage);
		mv.addObject("listcount", listcount);
		mv.addObject("rboardlist", rboardlist);
		mv.addObject("limit", limit);
		return mv;
	}

}
