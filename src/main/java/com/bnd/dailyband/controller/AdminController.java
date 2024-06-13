package com.bnd.dailyband.controller;

import com.bnd.dailyband.domain.Approval;
import com.bnd.dailyband.domain.ApvDoc;
import com.bnd.dailyband.domain.ApvRef;
import com.bnd.dailyband.domain.Bandhr;
import com.bnd.dailyband.domain.Member;
import com.bnd.dailyband.service.admin.AdminService;
import com.bnd.dailyband.service.rboard.RboardService;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
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

  //관리 navbar classappend
  private void addAdminAttributes(ModelAndView mv) {
    mv.addObject("current", "admin");
    mv.addObject("current_show", "admin");
  }

  //회원 관리
  @RequestMapping("/mbrmgmt")
  public ModelAndView memberList(ModelAndView mv) {
    addAdminAttributes(mv);
    mv.addObject("current_drop", "adminMbrMgmt");

    List<Map<String, Object>> mlist = adminService.getMemberList();

    mv.setViewName("admin/mbr_mgmt");
    mv.addObject("memberlist", mlist);
    return mv;
  }

  //회원 관리 - 회원 권한 변경
  @RequestMapping("/changeRole")
  public String changeRole(RedirectAttributes redirect, @RequestParam String id,
      @RequestParam String role) {
    logger.info("아이디 : " + id + " / 새 권한 : " + role);

    int result = adminService.changeType(id, role);

    if (result == 0) {
      redirect.addFlashAttribute("message", "회원 권한 변경에 실패하였습니다.");
      redirect.addFlashAttribute("status", "error");
    } else {
      redirect.addFlashAttribute("message", id + "님의 권한 변경을 완료하였습니다.");
      redirect.addFlashAttribute("status", "success");
    }

    return "redirect:/admin/mbrmgmt";
  }

  //회원 관리 - 회원 상태 변경
  @RequestMapping("/changeStatus")
  public String changeStatus(RedirectAttributes redirect, @RequestParam String id,
      @RequestParam int newStatus) {
    logger.info("회원 아이디: " + id + "/ 선택한 회원 상태 : " + newStatus);

    int result = adminService.changeStatus(id, newStatus);

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
  public ModelAndView dashboard(ModelAndView mv) {
    addAdminAttributes(mv);
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
    addAdminAttributes(mv);
    mv.addObject("current_drop", "adminRboardMgmt");

    List<Map<String, Object>> rList = adminService.getRboardList();

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
        case "R01":
          realm.append("기타/베이스 ");
          break;
        case "R02":
          realm.append("드럼 ");
          break;
        case "R03":
          realm.append("키보드 ");
          break;
        case "R04":
          realm.append("보컬 ");
          break;
        case "R05":
          realm.append("그 외 ");
          break;
        default:
          realm.append(id).append(" ");
      }
    }
    return realm.toString().trim();
  }


  //기안 문서
  @RequestMapping("/apv/draftList")
  public ModelAndView draftList(ModelAndView mv,
      @CurrentSecurityContext SecurityContext userPrincipal) {
    addAdminAttributes(mv);
    mv.addObject("current_drop_show", "adminApv");
    mv.addObject("current_drop", "draftList");

    String id = userPrincipal.getAuthentication().getName();

    List<ApvDoc> alist = adminService.getApvDraftList(id);

    mv.setViewName("admin/approval/draft_list");
    mv.addObject("draftList", alist);
    return mv;
  }

  //기안 문서 작성 페이지
  @RequestMapping("/apv/docWrite")
  public ModelAndView docWrite(ModelAndView mv,
      @CurrentSecurityContext SecurityContext userPrincipal) {
    addAdminAttributes(mv);
    mv.addObject("current_drop_show", "adminApv");
    mv.addObject("current_drop", "draftList");

    String id = userPrincipal.getAuthentication().getName();

    Date currentTime = new Date();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    //logger.info(sdf.format(currentTime));

    List<Map<String, Object>> forms = new ArrayList<>();

    Map<String, Object> form1 = new HashMap<>();
    form1.put("formNo", 0);
    form1.put("formName", "기안서");
    forms.add(form1);

    Map<String, Object> form2 = new HashMap<>();
    form2.put("formNo", 1);
    form2.put("formName", "품의서");
    forms.add(form2);

    Map<String, Object> form3 = new HashMap<>();
    form3.put("formNo", 2);
    form3.put("formName", "지출 결의서");
    forms.add(form3);

    mv.addObject("forms", forms);
    mv.addObject("currentTime", sdf.format(currentTime));
    mv.addObject("mbrId", id);

    mv.setViewName("admin/approval/doc_write");

    return mv;

  }

  //전자결재 결재자, 참조자 선택 모달
  @ResponseBody
  @RequestMapping(value = "/apv/mbrlist", method = RequestMethod.GET)
  public List<Member> modalMbrList(@CurrentSecurityContext SecurityContext userPrincipal) {

    String id = userPrincipal.getAuthentication().getName();
    List<Member> members = adminService.getApvMbrList(id);

    sanitizeMemberList(members);

    return members;
  }

  @ResponseBody
  @RequestMapping(value = "/apv/mbrSearch", method = RequestMethod.GET)
  public List<Member> modalMbrSearch(
      @RequestParam("searchKeyword") String searchKeyword,
      @CurrentSecurityContext SecurityContext userPrincipal) {
    String id = userPrincipal.getAuthentication().getName();

    List<Member> mSearchList = adminService.getApvMbrNcnmSearch(id, searchKeyword);

    sanitizeMemberList(mSearchList);

    return mSearchList;

  }

  //지역,장르,분야 null 처리
  private void sanitizeMemberList(List<Member> members) {
    for (Member member : members) {
      if (member.getMBR_ACT_REALM() == null) {
        member.setMBR_ACT_REALM("");
      }
      if (member.getMBR_PREFER_AREA() == null) {
        member.setMBR_PREFER_AREA("");
      }
      if (member.getMBR_PREFER_GENRE() == null) {
        member.setMBR_PREFER_GENRE("");
      }
    }
  }

  //결재문서, 임시 저장
  @RequestMapping(value = "/apv/add{Param}", method = RequestMethod.POST)
  public ModelAndView apvAdd(ModelAndView mv,
      @ModelAttribute Approval apv, @ModelAttribute ApvDoc apvDoc,
      @ModelAttribute ApvRef ref,
      @RequestParam(value = "apvMbrId", required = false) String apvMbrId,
      @RequestParam(value = "refMbrId", required = false) String refMbrId,
      @PathVariable("Param") String param,
      @CurrentSecurityContext SecurityContext userPrincipal) {

    String id = userPrincipal.getAuthentication().getName();
    boolean result = adminService.processApproval(apv, apvDoc, ref, apvMbrId, refMbrId, param, id);
    logger.info("apvMbrId: " + apvMbrId);
    logger.info("apvDoc: " + apvDoc);

    if (!result) {
      mv.addObject("message", "문서 처리에 실패하였습니다.");
    } else if (param.equals("Doc")) {
      mv.addObject("message", "결재 요청 완료");
    }

    mv.setViewName("redirect:./draftList");
    return mv;
  }
}
