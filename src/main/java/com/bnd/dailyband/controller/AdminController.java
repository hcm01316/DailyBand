package com.bnd.dailyband.controller;

import com.bnd.dailyband.domain.Bandhr;
import com.bnd.dailyband.domain.Member;
import com.bnd.dailyband.service.admin.AdminService;
import com.bnd.dailyband.service.rboard.RboardService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class AdminController {

  private static final Logger logger = LoggerFactory.getLogger(MemberController.class);

  private AdminService adminService;
  private RboardService rboardService;

  @Autowired
  public AdminController(AdminService adminService, RboardService rboardService) {
    this.adminService = adminService;
    this.rboardService = rboardService;
  }

  // 모든 요청에 대해 사용자 정보를 모델에 추가
  @ModelAttribute
  public void addAttributes(Model model, @AuthenticationPrincipal Member member) {
    if (member != null) {
      model.addAttribute("profilePhoto", member.getProfilePhoto());
      model.addAttribute("username", member.getUsername());
    }
  }

  //회원 관리
  @RequestMapping("/mbrmgmt")
  public ModelAndView memberList(ModelAndView mv) {
    mv.addObject("current", "admin");
    mv.addObject("current_show", "admin");
    mv.addObject("current_drop", "adminMbrMgmt");

    List<Member> list = adminService.getMemberList();

    mv.setViewName("admin/mbr_mgmt");
    mv.addObject("memberlist", list);
    return mv;
  }

  //회원 관리 - 회원 권한 변경
  @RequestMapping("/changeRole")
  public String changeRole(RedirectAttributes redirect, @RequestParam String id, @RequestParam String role) {
    logger.info("아이디 : " + id + " / 새 권한 : " + role);

    int result = adminService.changeType(id,role);

    if (result == 0) {
      redirect.addFlashAttribute("message", "회원 권한 변경에 실패하였습니다.");
      redirect.addFlashAttribute("status", "error");
    } else{
      redirect.addFlashAttribute("message", id + "님의 권한 변경을 완료하였습니다.");
      redirect.addFlashAttribute("status", "success");
    }

    return "redirect:/admin/mbrmgmt";
  }

  //회원 관리 - 회원 상태 변경
  @RequestMapping("/changeStatus")
  public String changeStatus(RedirectAttributes redirect, @RequestParam String id, @RequestParam int newStatus) {
    logger.info("회원 아이디: "+ id + "/ 선택한 회원 상태 : "+ newStatus);

    int result = adminService.changeStatus(id,newStatus);

    if (result == 0) {
      redirect.addFlashAttribute("message", "회원 상태 변경에 실패하였습니다.");
      redirect.addFlashAttribute("status", "error");
    } else {
      redirect.addFlashAttribute("message", id + "님의 상태 변경을 완료하였습니다.");
      redirect.addFlashAttribute("status", "success");
    }
    return "redirect:/admin/mbrmgmt";
  }

  //대시보드
  @RequestMapping("/dashboard")
  public ModelAndView dashboard (ModelAndView mv) {
    mv.addObject("current", "admin");
    mv.addObject("current_show", "admin");
    mv.addObject("current_drop", "adminDashboard");

    int TotalMbrCnt = adminService.getTotalMbrCount();
    int TodayMbrCnt = adminService.getTodayMbrCount();
    //logger.info("전체 회원 수 : " + TotalMbrCnt + " / 일일 회원 수 : "+ TodayMbrCnt);

    int TotalVisitCnt = adminService.getTotalVisitCount();
    int TodayVisitCnt = adminService.getTodayVisitCount();

    Map<String, Integer> mbrPreferAreaCnt = adminService.getPreferAreaCnt();
    Map<String, Integer> mbrPreferGenreCnt = adminService.getPreferGenreCnt();
    Map<String, Integer> mbrActRealmCnt = adminService.getActRealmCnt();
    //logger.info(mbrPreferAreaCnt.toString());

    List<Map<String, Object>> dayRegCnt = adminService.getDayRegCnt();
    List<Map<String, Object>> dayVisitCnt = adminService.getDayVisitCnt();
    //logger.info(dayRegCnt.toString());
    List<Map<String, Object>> bbsCnt = adminService.getBbsCnt();
    //logger.info(bbsCnt.toString());

    mv.setViewName("admin/dashboard");
    mv.addObject("TotalMbrCnt", TotalMbrCnt);
    mv.addObject("TodayMbrCnt", TodayMbrCnt);
    mv.addObject("TotalVisitCnt", TotalVisitCnt);
    mv.addObject("TodayVisitCnt", TodayVisitCnt);
    mv.addObject("mbrPreferAreaCnt", mbrPreferAreaCnt);
    mv.addObject("mbrPreferGenreCnt", mbrPreferGenreCnt);
    mv.addObject("mbrActRealmCnt", mbrActRealmCnt);
    mv.addObject("dayRegCnt", dayRegCnt);
    mv.addObject("dayVisitCnt", dayVisitCnt);
    mv.addObject("bbsCnt", bbsCnt);
    return mv;
  }

  //밴드원 모집 관리
  @RequestMapping("/rboardmgmt")
  public ModelAndView rboardMgmt(ModelAndView mv) {
    mv.addObject("current", "admin");
    mv.addObject("current_show", "admin");
    mv.addObject("current_drop", "adminRboardMgmt");

    List<Map<String, Object>> rList= adminService.getRboardList();

    Map<Integer, List<Bandhr>> participantsMap = new HashMap<>();
    Map<Integer, List<Bandhr>> joinListMap = new HashMap<>();

    for (Map<String, Object> r : rList) {
      String areaId = (String) r.get("RCRIT_AREA_ID");
      String genreId = (String) r.get("RCRIT_GENRE_ID");
      String realmId = (String) r.get("RCRIT_REALM_ID");

      r.put("RCRIT_AREA_IDK", convertAreaId(areaId));
      r.put("RCRIT_GENRE_IDK", convertGenreId(genreId));
      r.put("RCRIT_REALM_IDK", convertRealmId(realmId));

      int bbsSn = (int) r.get("BBS_SN");
      List<Bandhr> bandList = rboardService.getbandList(bbsSn);
      List<Bandhr> joinList = rboardService.getjoinlist(bbsSn);

      participantsMap.put(bbsSn, bandList);
      joinListMap.put(bbsSn, joinList);

      logger.info(participantsMap.toString());
    }

    mv.setViewName("admin/rboardinfo_mgmt");
    mv.addObject("rBoardList", rList);
    mv.addObject("participantsMap", participantsMap);
    mv.addObject("joinListMap", joinListMap);
    return mv;
  }

  private Object convertAreaId(String areaId) {
    return switch (areaId) {
      case "A01" -> "서울";
      case "A02" -> "경기";
      case "A03" -> "부산";
      case "A04" -> "대구";
      case "A05" -> "광주";
      case "A06" -> "대전";
      case "A07" -> "충남";
      case "A08" -> "충북";
      case "A09" -> "세종";
      case "A10" -> "울산";
      case "A11" -> "인천";
      case "A12" -> "강원";
      case "A13" -> "전남";
      case "A14" -> "전북";
      case "A15" -> "경북";
      case "A16" -> "경남";
      case "A17" -> "제주";
      default -> areaId;
    };
  }

  private String convertGenreId(String genreId) {
    return switch (genreId) {
      case "G01" -> "팝";
      case "G02" -> "발라드";
      case "G03" -> "인디음악";
      case "G04" -> "랩·힙합";
      case "G05" -> "K-POP";
      case "G06" -> "트로트";
      case "G07" -> "일렉트로닉";
      case "G08" -> "락";
      case "G09" -> "메탈";
      case "G10" -> "R&B";
      case "G11" -> "재즈";
      case "G12" -> "클래식";
      case "G13" -> "뮤지컬";
      case "G14" -> "국악";
      case "G15" -> "J-POP";
      case "G16" -> "월드뮤직";
      default -> genreId;
    };
  }

  private String convertRealmId(String realmId) {
    String[] ids = realmId.split(",");
    StringBuilder realm = new StringBuilder();
    for (String id : ids) {
      switch (id) {
        case "R01": realm.append("기타/베이스 "); break;
        case "R02": realm.append("드럼 "); break;
        case "R03": realm.append("키보드 "); break;
        case "R04": realm.append("보컬 "); break;
        case "R05": realm.append("그 외 "); break;
        default: realm.append(id).append(" ");
      }
    }
    return realm.toString().trim();
  }

}
