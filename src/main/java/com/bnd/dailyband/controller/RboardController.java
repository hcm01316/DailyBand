package com.bnd.dailyband.controller;

import com.bnd.dailyband.domain.Ctgry;
import com.bnd.dailyband.domain.Rboard;
import com.bnd.dailyband.service.rboard.RboardService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;

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
			mv.setViewName("rboard/rboardview");
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
		mv.setViewName("rboard/rboardwrite");
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
	
}
