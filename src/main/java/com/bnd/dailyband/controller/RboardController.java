package com.bnd.dailyband.controller;

import com.bnd.dailyband.domain.*;
import com.bnd.dailyband.service.admin.AdminService;
import com.bnd.dailyband.service.chat.ChatService;
import com.bnd.dailyband.service.notify.SseService;
import com.bnd.dailyband.service.rboard.RboardService;
import com.bnd.dailyband.service.s3upload.ImageUploadService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/rboard")
public class RboardController {

	private static final Logger logger = LoggerFactory.getLogger(RboardController.class);

	private RboardService rboardService;
	private ImageUploadService imageUploadService;
	private ChatService chatService;
	private SseService sseService;
	private AdminService adminService;

	@Autowired
	public RboardController(RboardService rboardService, ImageUploadService imageUploadService,
			ChatService chatService, SseService sseService, AdminService adminService)
	{
		this.rboardService = rboardService;
		this.imageUploadService = imageUploadService;
		this.chatService = chatService;
		this.sseService = sseService;
		this.adminService = adminService;
	}

	@ModelAttribute
	public void addAttributes(Model model, @AuthenticationPrincipal Member member) {
		int resCnt = adminService.resWaitCnt();
		model.addAttribute("resCnt", resCnt);

		if (member != null) {
			model.addAttribute("profilePhoto", member.getProfilePhoto());
			model.addAttribute("username", member.getUsername());
		}
	}


	@GetMapping("/detail")
	public ModelAndView Detail(
			int num, ModelAndView mv,
			HttpServletRequest request, @RequestHeader(value ="referer", required= false)String beforeURL,@CurrentSecurityContext SecurityContext userPrincipal) {

		logger.info("regerer:" + beforeURL);
		if(beforeURL!=null && beforeURL.endsWith("list")) {
			rboardService.setReadCountUpdate(num);
		}

		Rboard rboard = rboardService.getDetail(num);
		String id = userPrincipal.getAuthentication().getName();
		int joinck = rboardService.JoinCk(num,id);
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
			int MyBandChat = rboardService.MyBandChat(rboard.getBAND_TEAM_NM());
			mv.addObject("MyBandChat",MyBandChat);
			mv.setViewName("rboard/rboard_view");
			mv.addObject("current","rBoard");
			mv.addObject("rboarddata", rboard);
			mv.addObject("cnt", cnt);
			mv.addObject("joinck",joinck);
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
		int hc = rboard.getRCRIT_NOPE()+1;
		String chatname = rboard.getBAND_TEAM_NM();
		rboard.setRCRIT_REALM_ID(trealm);
		rboardService.insertRboard(rboard);

		int num = rboardService.getaddnum();
		rboardService.insertBand(id,num);
		rboardService.BandChatRoomCreate(chatname,hc);

		int chatnum = rboardService.getChatNum();// 채팅방 생성
		chatService.ChatJoin(id,chatnum); // 리더 채팅방 가입

		logger.info(rboard.toString()); // selectKey로 정의한 BOARD_NUM 값 확인해 봅시다.
		return "redirect:list";
	}


	@RequestMapping ("/bandHR")
	public ModelAndView mgmtlist(ModelAndView mv, HttpServletRequest request,
								 @CurrentSecurityContext SecurityContext userPrincipal) {

		String id = userPrincipal.getAuthentication().getName();
		mv.addObject("id", id);
		int bandck = rboardService.bandck(id);
		mv.addObject("bandck", bandck);
		mv.addObject("current","bandHR");

		if (bandck == -1) {
			mv.addObject("isband", 0);
		}
		if (bandck != -1)
		{
			int myband = rboardService.myband(id);
			String TeamName =rboardService.BandTeamName(myband);
			//System.out.println(bandck);
			int MyBandChat = rboardService.MyBandChat(TeamName);
			mv.addObject("MyBandChat", MyBandChat);
			//System.out.println(MyBandChat);
			int leaderck = rboardService.leaderck(id);
			mv.addObject("leaderck",leaderck);
			List<Bandhr> bandlist = new ArrayList<Bandhr>();
			bandlist = rboardService.getbandList(myband);
			mv.addObject("bandlist", bandlist);
			List<Bandhr> joinlist = new ArrayList<Bandhr>();
			joinlist = rboardService.getjoinlist(myband);
			mv.addObject("joinlist", joinlist);
			logger.info(bandlist.toString());
			List<Reservation> resList = rboardService.getRoomResList(myband);

			logger.info(resList.toString());
			mv.addObject("resList",resList);

		}
		mv.setViewName("rboard/rboard_mgmt");
		return mv;
	}


	@RequestMapping(value= "/list", method=RequestMethod.GET)
	public ModelAndView boardList(@RequestParam(value="page",defaultValue="1") int page, ModelAndView mv,
								  @CurrentSecurityContext SecurityContext userPrincipal) {

		String id = userPrincipal.getAuthentication().getName();
		int limit = 6; // 한 화면에 출력할 로우 갯수

		int listcount = rboardService.getListCount(); // 총 리스트 수를 받아옴

		//총 페이지 수
		int maxpage = (listcount + limit - 1) / limit;

		//현재 페이지에 보여줄 시작 페이지 수 (1, 11, 21 등...)
		int startpage = ((page - 1) / 10) * 10 + 1;

		int endpage = startpage + 10 - 1;
		if (endpage > maxpage)
			endpage = maxpage;

		List<Rlist> rboardlist = rboardService.getRboardList(page, limit);// 리스트를 받아옴

		for (Rlist vo : rboardlist) {
			System.out.println(vo.getBBS_CN());
			String imageUrl = getImgSrc(vo.getBBS_CN());
			vo.setImageUrl(imageUrl.isEmpty() ? "/image/rboard.png" : imageUrl);
		}





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

	public String getImgSrc(String content) {
		if (content != null) {
			Pattern imgTagPattern = Pattern.compile("<img\\s+[^>]*src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>");
			Matcher matcher = imgTagPattern.matcher(content);
			if (matcher.find()) {
				return matcher.group(1);
			}
		}
		return "";
	}

	@RequestMapping ("/join")
	public ModelAndView join(ModelAndView mv, HttpServletRequest request,int num,Principal userPrincipal, RedirectAttributes redirect,@AuthenticationPrincipal Member member) {

		String id = userPrincipal.getName();
		int isjoin = rboardService.isjoin(id);

		if (isjoin == 0) {
			rboardService.join(id,num);
			redirect.addFlashAttribute("message", "가입 신청이 성공적으로 완료되었습니다.");
			String leader = rboardService.getleader(num);
			sseService.sendNotification(leader, member.getMBR_NCNM() + "님이 가입 신청하셨습니다.", "rboard/bandHR" , 2);
			redirect.addFlashAttribute("status", "success");
		}

		if (isjoin >= 1) {
			redirect.addFlashAttribute("message", "이미 신청을 했거나 밴드에 참여중 입니다.");
			redirect.addFlashAttribute("status", "error");
		}
		mv.addObject("num", num);
		mv.setViewName("redirect:detail");
		return mv;
	}

	@RequestMapping ("/joincancel")
	public ModelAndView joincancel(ModelAndView mv, HttpServletRequest request,int num,Principal userPrincipal, RedirectAttributes redirect,@AuthenticationPrincipal Member member) {

		String id = userPrincipal.getName();
		int joincancel = rboardService.JoinCancel(num,id);

		if (joincancel == 1) {
			redirect.addFlashAttribute("message", "가입 취소가 성공적으로 완료되었습니다.");
			String leader = rboardService.getleader(num);
			sseService.sendNotification(leader, member.getMBR_NCNM() + "님이 가입 신청을 취소 하셨습니다.", "rboard/bandHR" , 2);
			redirect.addFlashAttribute("status", "success");
		}

		if (joincancel == 0) {
			redirect.addFlashAttribute("message", "가입 취소에 실패 했습니다.");
			redirect.addFlashAttribute("status", "error");
		}
		mv.addObject("num", num);
		mv.setViewName("redirect:detail");
		return mv;
	}

	@RequestMapping ("/resign")
	public ModelAndView resign(ModelAndView mv, HttpServletRequest request,String id, int num, RedirectAttributes redirect,int chat) {

		int resign = rboardService.resign(id,num);

		int cnt = rboardService.bandacceptcnt(num);
		int nope = rboardService.getrenope(num);

		if(cnt != nope)
		{
			rboardService.teamstopen(num);
		}

		if (resign == 0) {
			redirect.addFlashAttribute("message", "강퇴에 실패 했습니다.");
			redirect.addFlashAttribute("status", "error");
		}

		if (resign == 1) {
			redirect.addFlashAttribute("message", "성공적으로 강퇴 하였습니다.");
			chatService.ChatExit(id,chat);
			String TeamName = rboardService.BandTeamName(num);
			sseService.sendNotification(id, TeamName + "에서 강퇴 당했습니다.", "rboard/bandHR" , 2);
			redirect.addFlashAttribute("status", "success");
		}
		mv.setViewName("redirect:" + request.getHeader("Referer"));
		return mv;
	}

	@RequestMapping ("/accept")
	public String accept(HttpServletRequest request,String id, int num, RedirectAttributes redirect,int chat) {

		int accept = rboardService.bandaccept(id,num);

		if (accept == 0) {
			redirect.addFlashAttribute("message", "수락에 실패했습니다.");
			redirect.addFlashAttribute("status", "error");
		}

		if (accept == 1) {
			redirect.addFlashAttribute("message", "성공적으로 수락 하였습니다.");
			String TeamName = rboardService.BandTeamName(num);
			sseService.sendNotification(id, TeamName + " 가입 승인 되었습니다.", "rboard/bandHR" , 2);
			redirect.addFlashAttribute("status", "success");

			rboardService.BandChatJoin(chat,id);
			int cnt = rboardService.bandacceptcnt(num);
			logger.info("cnt : "+cnt);
			int nope = rboardService.getrenope(num);
			logger.info("nope : "+nope);

			if(cnt == nope)
			{
				rboardService.joinwatingdel(num);
				rboardService.teamstclose(num);
			}
		}
		return "redirect:" + request.getHeader("Referer");
	}

	@RequestMapping ("/refuse")
	public String refuse(HttpServletRequest request,String id, int num, RedirectAttributes redirect) {



		int refuse = rboardService.refuse(id,num);

		if (refuse == 0) {
			redirect.addFlashAttribute("message", "거절에 실패했습니다.");
			redirect.addFlashAttribute("status", "error");
		}

		if (refuse == 1) {
			redirect.addFlashAttribute("message", "성공적으로 거절 하였습니다.");
			String TeamName = rboardService.BandTeamName(num);
			sseService.sendNotification(id, TeamName + " 가입 거절 되었습니다.", "rboard/bandHR" , 2);
			redirect.addFlashAttribute("status", "success");
		}
		return "redirect:" + request.getHeader("Referer");
	}

	@RequestMapping ("/breakup")
	public ModelAndView breakup(ModelAndView mv, HttpServletRequest request,RedirectAttributes redirect,Principal userPrincipal,int chat) {

		String id = userPrincipal.getName();

		int myband = rboardService.myband(id);

		List<String> bandlist = rboardService.bandlist(myband);
		String TeamName = rboardService.BandTeamName(myband);



		int breakup = rboardService.breakup(myband);

		if (breakup == 0) {
			redirect.addFlashAttribute("message", "해체에 실패했습니다.");
			redirect.addFlashAttribute("status", "error");
		}

		if (breakup == 1) {
			redirect.addFlashAttribute("message", "성공적으로 해체 하였습니다.");
			chatService.ChatRoomDelete(chat);
			for (String MBR_ID : bandlist) {
				sseService.sendNotification(MBR_ID, TeamName + " 가 해체 되었습니다.", "rboard/bandHR", 2);
			}
			redirect.addFlashAttribute("status", "success");
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
	public ModelAndView delete(ModelAndView mv, HttpServletRequest request,int num,RedirectAttributes redirect,int chat) {

		List<String> bandlist = rboardService.bandlist(num);
		String TeamName = rboardService.BandTeamName(num);
		int delete = rboardService.breakup(num);


		if (delete == 0) {
			redirect.addFlashAttribute("message", "게시글 삭제에 실패 했습니다.");
			redirect.addFlashAttribute("status", "error");
		}

		if (delete == 1) {
			redirect.addFlashAttribute("message", "게시글 삭제에 성공 하였습니다.");
			chatService.ChatRoomDelete(chat);
			for (String MBR_ID : bandlist) {
				sseService.sendNotification(MBR_ID, TeamName + " 가 해체 되었습니다.", "rboard/bandHR", 2);
			}
			redirect.addFlashAttribute("status", "success");
		}

		String referer = request.getHeader("Referer");
		if (referer.contains("detail")) {
			mv.setViewName("redirect:list");
		} else {
			mv.setViewName("redirect:" + referer);
		}
		return mv;
	}

	@RequestMapping ("/leave")
	public ModelAndView leave(ModelAndView mv, HttpServletRequest request,int num,RedirectAttributes redirect,Principal userPrincipal,int chat,@AuthenticationPrincipal Member member) {

		String id = userPrincipal.getName();
		int leave = rboardService.leave(id,num);

		int cnt = rboardService.bandacceptcnt(num);
		int nope = rboardService.getrenope(num);

		if(cnt != nope)
		{
			rboardService.teamstopen(num);
		}

		if (leave == 0) {
			redirect.addFlashAttribute("message", "탈퇴에 실패 했습니다.");
			redirect.addFlashAttribute("status", "error");
		}

		if (leave  == 1) {
			redirect.addFlashAttribute("message", "성공적으로 탈퇴 했습니다.");
			chatService.ChatExit(id,chat);
			String TeamName = rboardService.BandTeamName(num);
			String leader = rboardService.getleader(num);
			sseService.sendNotification(leader, member.getMBR_NCNM() + "님이 " + TeamName + "를 탈퇴 하셨습니다.", "rboard/bandHR" , 2);
			redirect.addFlashAttribute("status", "success");
		}
		mv.setViewName("redirect:bandHR");
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

	@RequestMapping("/upload")
	@ResponseBody
	public ResponseEntity<Map<String, String>> upload(MultipartFile upload) throws IOException {
		// 이미지 업로드 로직
		String imageUrl = imageUploadService.upload(upload);

		System.out.println("Received file: " + upload.getOriginalFilename());
		System.out.println("File size: " + upload.getSize());

		Map<String, String> response = new HashMap<>();
		response.put("url", imageUrl);
		// 이미지 URL 반환
		return ResponseEntity.ok(response);
	}

	@ResponseBody
	@RequestMapping(value = "/BandTeamNameCheck", method = RequestMethod.GET)
	public int BandTeamNameCheck(@RequestParam("BAND_TEAM_NM") String BAND_TEAM_NM){
		return rboardService.isTeamName(BAND_TEAM_NM);
	}

}