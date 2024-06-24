package com.bnd.dailyband.controller;

import com.bnd.dailyband.domain.Approval;
import com.bnd.dailyband.domain.ApvDoc;
import com.bnd.dailyband.domain.ApvRef;
import com.bnd.dailyband.domain.Bandhr;
import com.bnd.dailyband.domain.Member;
import com.bnd.dailyband.service.admin.AdminService;
import com.bnd.dailyband.service.notify.SseService;
import com.bnd.dailyband.service.rboard.RboardService;
import jakarta.servlet.http.HttpServletRequest;
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
import org.springframework.web.bind.annotation.GetMapping;
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
  private SseService sseService;

  @Autowired
  public AdminController(AdminService adminService, RboardService rboardService,
      SseService sseService) {
    this.adminService = adminService;
    this.rboardService = rboardService;
    this.sseService = sseService;
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

    String chrole = "";
    switch (role) {
      case "ROLE_ADMIN":
        chrole = "관리자";
        break;
      case "ROLE_MANAGER":
        chrole = "매니저";
        break;
      case "ROLE_MEMBER":
        chrole = "회원";
        break;
    }

    if (result == 0) {
      redirect.addFlashAttribute("message", "회원 권한 변경에 실패하였습니다.");
      redirect.addFlashAttribute("status", "error");
    } else {
      redirect.addFlashAttribute("message", id + "님의 권한 변경을 완료하였습니다.");
      redirect.addFlashAttribute("status", "success");
      sseService.sendNotification(id, "회원님의 권한이 " + chrole + "(으)로 변경되었습니다.", "main", 5);
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
  public ModelAndView dashboard(ModelAndView mv,
      @CurrentSecurityContext SecurityContext userPrincipal) {
    addAdminAttributes(mv);
    mv.addObject("current_drop", "adminDashboard");

    String id = userPrincipal.getAuthentication().getName();

    int TotalMbrCnt = adminService.getTotalMbrCount();
    int TodayMbrCnt = adminService.getTodayMbrCount();
    //logger.info("전체 회원 수 : " + TotalMbrCnt + " / 일일 회원 수 : "+ TodayMbrCnt);

    int TotalVisitCnt = adminService.getTotalVisitCount();
    int TodayVisitCnt = adminService.getTodayVisitCount();

    int ApvWaitCnt = adminService.getApvWaitCnt(id);
    int ApvScheduledCnt = adminService.getApvScheduledCnt(id);

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
    mv.addObject("ApvWaitCnt", ApvWaitCnt);
    mv.addObject("ApvScheduledCnt", ApvScheduledCnt);
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

  @RequestMapping("/gboardmgmt")
  public ModelAndView gboardmgmt(ModelAndView mv) {
    addAdminAttributes(mv);
    mv.addObject("current_drop", "adminGboardMgmt");

    List<Map<String, Object>> gBoardList = adminService.getGboardList();
    List<Map<String, Object>> gBoardCmntList = adminService.getGboardCmntList();

    mv.addObject("gBoardList", gBoardList);
    mv.addObject("gBoardCmntList", gBoardCmntList);

    mv.setViewName("admin/gboardinfo_mgmt");

    return mv;
  }

  @RequestMapping("/gboardDelete")
  public ModelAndView gboardDelete(ModelAndView mv, int num,RedirectAttributes redirect) {

    int delete = adminService.gBoardDelete(num);

    if (delete == 0) {
      redirect.addFlashAttribute("message", "게시글 삭제에 실패 했습니다.");
      redirect.addFlashAttribute("status", "error");
    } else {
      redirect.addFlashAttribute("message", "게시글 삭제를 완료 했습니다.");
      redirect.addFlashAttribute("status", "success");
    }

    mv.setViewName("redirect:gboardmgmt");
    return mv;
  }

  @RequestMapping("/gboardCmntDelete")
  public ModelAndView gboardCmntDelete(ModelAndView mv, int num, RedirectAttributes redirect) {
    int cmntDelete = adminService.gBoardCmntDelete(num);

    if (cmntDelete == 0) {
      redirect.addFlashAttribute("message", "댓글 삭제에 실패 했습니다.");
      redirect.addFlashAttribute("status", "error");
    } else {
      redirect.addFlashAttribute("message", "댓글 삭제를 완료 했습니다.");
      redirect.addFlashAttribute("status", "success");
    }

    mv.setViewName("redirect:gboardmgmt?tab=comments");
    return mv;
  }

//  @ResponseBody
//  @GetMapping("/loadbbs")
//  public List<Map<String, Object>> loadBbs() {
//    return adminService.getGboardList();
//  }

//  @ResponseBody
//  @GetMapping("/loadcomments")
//  public List<Map<String, Object>> loadComments() {
//    return adminService.getGboardCmntList();
//  }


  //기안, 결재, 참조, 임시 문서 리스트
  @RequestMapping("apv/{param}List")
  public ModelAndView draftList(ModelAndView mv, @PathVariable("param") String param,
      @RequestParam(value = "docSttus", required = false) String docSttus,
      @ModelAttribute ApvDoc apvDoc, @ModelAttribute Approval apv,
      @ModelAttribute ApvRef ref,
      @CurrentSecurityContext SecurityContext userPrincipal) {
    addAdminAttributes(mv);
    mv.addObject("current_drop_show", "adminApv");

    if (param.equals("temp")) {
      docSttus = "4"; //임시
    } else if (docSttus == null || docSttus.isEmpty()) {
      docSttus = "5";
    }
    mv.addObject("docSttus", docSttus);
    String id = userPrincipal.getAuthentication().getName();
    List<ApvDoc> apvDocList = null;

    logger.info("docSttus 현재 상태 : " + docSttus);
    switch (param) {
      case "draft" -> {
        apvDocList = adminService.getApvDraftList(id);
      }
      case "temp" -> {
        apvDocList = adminService.getApvTempList(id);
      }
      case "apv" -> {
        apvDocList = adminService.getApvList(id, docSttus);
      }
      case "ref" -> {
        apvDocList = adminService.getApvRefList(id, docSttus);
      }
    }
    logger.info(param + " - apvDoc : " + apvDocList);
    mv.addObject("current_drop", param + "List");
    mv.addObject("apvDoc", apvDocList);
    mv.addObject("type", param);
    mv.setViewName("admin/apv/" + param + "_list");
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
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    //logger.info(sdf.format(currentTime));

    List<Map<String, Object>> forms = getFormList();

    mv.addObject("forms", forms);
    mv.addObject("currentTime", sdf.format(currentTime));
    mv.addObject("mbrId", id);

    mv.setViewName("admin/apv/doc_write");

    return mv;

  }

  private List<Map<String, Object>> getFormList() {
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

    return forms;
  }

  //결재자, 참조자 선택 모달
  @ResponseBody
  @RequestMapping(value = "/apv/mbrlist", method = RequestMethod.GET)
  public List<Member> modalMbrList(@CurrentSecurityContext SecurityContext userPrincipal) {

    String id = userPrincipal.getAuthentication().getName();
    List<Member> members = adminService.getApvMbrList(id);

    sanitizeMemberList(members);

    return members;
  }

  //결재자, 참조자 선택 모달 닉네임 검색
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

  //결재문서, 임시 저장, 재상신, 반려문서 임시 저장
  @RequestMapping(value = "/apv/add{param}", method = RequestMethod.POST)
  public ModelAndView apvAdd(ModelAndView mv, RedirectAttributes redirect,
      @ModelAttribute Approval apv, @ModelAttribute ApvDoc apvDoc,
      @ModelAttribute ApvRef ref,
      @RequestParam(value = "apvMbrId", required = false) String apvMbrId,
      @RequestParam(value = "refMbrId", required = false) String refMbrId,
      @PathVariable("param") String param,
      @CurrentSecurityContext SecurityContext userPrincipal) {

    String id = userPrincipal.getAuthentication().getName();
    apvDoc.setMBR_ID(id);

    if (param.equals("Doc") || param.equals("RejDoc")) {
      apvDoc.setDOC_STTUS("0"); // 대기
    } else if (param.equals("Temp") || param.equals("RejTemp")) {
      apvDoc.setDOC_STTUS("4"); // 임시
    }

    int docResult = adminService.insertDoc(apvDoc);

    int apvResult = 0;
    int refResult = 0;


    logger.info("문서 apvDoc.getMBR_ID() 값 : " + apvDoc.getMBR_ID());
    if (!apvMbrId.equals("apvMbrId") || param.equals("Temp") || param.equals("Doc")) {
      String[] apvArray = apvMbrId.split(",");
      for (int i = 0; i < apvArray.length; i++) {
        apv.setDOC_SN(0);
        apv.setMBR_ID(apvArray[i]);
        apv.setAPV_LEV(i + 1);
        if (!param.equals("Temp")) {
          if (i == 0) {
            apv.setAPV_STTUS(0); //대기
            sseService.sendNotification(apv.getMBR_ID(), "새 문서의 결재 요청이 있습니다.", "admin/apv/apvList",
                5);
          } else {
            apv.setAPV_STTUS(1); //예정
          }
        } else if (param.equals("Temp")) {
          apv.setAPV_STTUS(5); //임시
        }
        apvResult = adminService.insertApv(apv); // 결재자 등록
      }
    } else { // 반려된 문서 재상신 - 결재자 선택 안함
      List<Approval> apvList = adminService.getApvDetail(apvDoc.getDOC_SN());
      if (!apvList.isEmpty()) {
        for (int i = 0; i < apvList.size(); i++) {
          apv.setDOC_SN(0);
          apv.setMBR_ID(apvList.get(i).getMBR_ID());
          apv.setAPV_LEV(apvList.get(i).getAPV_LEV());
          if (!param.equals("RejTemp")) {
            if (i == 0) {
              apv.setAPV_STTUS(0);
              sseService.sendNotification(apv.getMBR_ID(), "새 문서 결재 요청이 있습니다.", "admin/apv/apvList", 5);
            } else {
              apv.setAPV_STTUS(1);
            }
          } else if (param.equals("RejTemp")) {
            apv.setAPV_STTUS(5);
          }
          apvResult = adminService.insertApv(apv);
        }
      }
    }

    //참조자
    if (param.equals("RejDoc") || param.equals("RejTemp")) { // 재상신
      if (refMbrId.equals("refMbrId")) {
        List<ApvRef> refList = adminService.getRefDetail(apvDoc.getDOC_SN());
        if (!refList.isEmpty()) {
          for (int i = 0; i < refList.size(); i++) {
            ref.setMBR_ID(refList.get(i).getMBR_ID());
            ref.setDOC_SN(0);
            if (!param.equals("RejTemp")) {
              ref.setREF_STTUS(0);
            } else {
              ref.setREF_STTUS(1);
            }
            refResult = adminService.insertRef(ref);
          }
        } else {
          refResult = 1;
        }
      } else if (!refMbrId.equals("refMbrId") && !refMbrId.equals("")) {
        String[] refArray = refMbrId.split(",");
        for (int i = 0; i < refArray.length; i++) {
          ref.setMBR_ID(refArray[i]);
          ref.setDOC_SN(0);
          if (!param.equals("RejTemp")) {
            ref.setREF_STTUS(0);
          } else {
            ref.setREF_STTUS(1);
          }
          refResult = adminService.insertRef(ref);
        }
      } else if (refMbrId.equals("")) {
        refResult = 1;
      }
    } else {
      if (!refMbrId.isEmpty()) {
        String[] refArray = refMbrId.split(",");
        for (int i = 0; i < refArray.length; i++) {
          ref.setMBR_ID(refArray[i]);
          ref.setDOC_SN(0);
          if (!param.equals("Temp")) {
            ref.setREF_STTUS(0);
          } else {
            ref.setREF_STTUS(1);
          }
          refResult = adminService.insertRef(ref);
        }
    } else { // refMbrId가 비어있을 경우
        refResult = 1;
      }
    }

    if (docResult < 1) {
      logger.info("문서 처리 실패");
    } else if (apvResult < 1) {
      logger.info("결재자 등록 실패");
    } else if (refResult < 1) {
      logger.info("참조자 등록 실패");
    } else {
      if (param.equals("Doc") || param.equals("RejDoc")) {
        redirect.addFlashAttribute("message", "결재 요청을 완료하였습니다.");
        redirect.addFlashAttribute("status", "success");
        mv.setViewName("redirect:draftList");
      } else if (param.equals("Temp") || param.equals("RejTemp")) {
        redirect.addFlashAttribute("message", "임시 저장을 완료하였습니다.");
        redirect.addFlashAttribute("status", "success");
        mv.setViewName("redirect:tempList");
      }
    }

    logger.info("문서에서 가져온  DOC_SN 값 = " + apvDoc.getDOC_SN());

    logger.info("add(apvMbrId): " + apvMbrId);
    logger.info("add(apvDoc): " + apvDoc);
    logger.info("add(param) : " + param);

    return mv;
  }

  //문서 상세 페이지
  @RequestMapping("/apv/docDetail")
  public ModelAndView docDetail(ModelAndView mv, HttpServletRequest request,
      @RequestParam("docSn") int docSn, @RequestParam("type") String type,
      @RequestParam(value = "docSttus", required = false) String docSttus) {
    addAdminAttributes(mv);
    mv.addObject("current_drop_show", "adminApv");
    mv.addObject("current_drop", type + "List");

    Date currentTime = new Date();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    mv.addObject("currentTime", sdf.format(currentTime));

    ApvDoc apvDoc = adminService.getDocDetail(docSn);
    List<Approval> apvList = adminService.getApvDetail(docSn);
    List<ApvRef> refList = adminService.getRefDetail(docSn);

    logger.info(apvDoc.toString());
    logger.info(apvList.toString());
    logger.info(refList.toString());

    if (!apvList.isEmpty() && apvDoc != null) {
      mv.addObject("apvDoc", apvDoc);
      mv.addObject("apvList", apvList);
      mv.addObject("refList", refList);
      mv.addObject("type", type);
      logger.info("type : " + type);
      logger.info("docSttus : " + docSttus);
      if (!docSttus.equals("")) {
        mv.addObject("docSttus", docSttus);
      }
      if (type.equals("draft") || type.equals("apv") || type.equals("ref")) {
        mv.setViewName("admin/apv/doc_detail");
      } else if (type.equals("temp") || type.equals("rej")) {
        mv.setViewName("admin/apv/doc_update");
      }

    } else {
      mv.addObject("message", "문서 조회 실패");
      logger.info("문서 조회 실패");
      mv.setViewName("redirect:" + type + "List");
    }

    return mv;
  }

  //문서 삭제(상신 취소)
  @RequestMapping("/apv/docDelete")
  public String docDelete(Model model, @RequestParam("docSn") int docSn,
      RedirectAttributes redirect, @RequestParam("type") String type) {
    int result = adminService.deleteDoc(docSn);

    logger.info("삭제 delete :" + type);
    if (result > 0) {
      if (type.equals("draft")) {
        redirect.addFlashAttribute("message", "상신 취소를 완료하였습니다.");
        redirect.addFlashAttribute("status", "success");
      } else if (type.equals("temp")) {
        redirect.addFlashAttribute("message", "문서 삭제를 완료하였습니다.");
        redirect.addFlashAttribute("status", "success");
      }
      return "redirect:./" + type + "List";
    } else {
      if (type.equals("draft")) {
        redirect.addFlashAttribute("message", "상신 취소에 실패하였습니다.");
        redirect.addFlashAttribute("status", "error");
      } else if (type.equals("temp")) {
        redirect.addFlashAttribute("message", "문서 삭제에 실패하였습니다.");
        redirect.addFlashAttribute("status", "error");
      }
      return "redirect:./" + type + "List";
    }
  }

  //임시 저장 문서 수정
  @RequestMapping(value = "/apv/update{param}", method = RequestMethod.POST)
  public ModelAndView docUpdate(ModelAndView mv,
      @ModelAttribute Approval apv, @ModelAttribute ApvDoc apvDoc,
      @ModelAttribute ApvRef ref,
      @RequestParam(value = "apvMbrId", required = false) String apvMbrId,
      @RequestParam(value = "refMbrId", required = false) String refMbrId,
      @PathVariable("param") String param,
      @CurrentSecurityContext SecurityContext userPrincipal) {

    logger.info("update 현재 param : " + param);

    String id = userPrincipal.getAuthentication().getName();
    logger.info("문서 번호 DOC_SN : " + apvDoc.getDOC_SN());
    logger.info("문서 양식 타입 DOC_FORM_TY : " + apvDoc.getDOC_FORM_TY());
    logger.info("문서 작성자 아이디 MBR_ID : " + id);
    logger.info("문서 작성자 닉네임 MBR_NCNM : " + apvDoc.getMBR_NCNM());
    logger.info("문서 작성자 타입 MBR_TY : " + apvDoc.getMBR_TY());
    logger.info("문서 제목 DOC_SJ : " + apvDoc.getDOC_SJ());
    logger.info("문서 내용 DOC_CN : " + apvDoc.getDOC_CN());
    logger.info("문서 상태 DOC_STTUS : " + apvDoc.getDOC_STTUS());
    logger.info("문서 작성 날짜 DOC_WRT_DT : " + apvDoc.getDOC_WRT_DT());
    logger.info("결재 상태 APV_STTUS : " + apvDoc.getAPV_STTUS());

    String mbrNcNm = adminService.getMbrNCNM(apvDoc.getDOC_SN());


    if (param.equals("Doc")) {
      apvDoc.setDOC_STTUS("0"); //대기
    } else if (param.equals("Temp")) {
      apvDoc.setDOC_STTUS("4"); //임시
    }

    int docResult = adminService.modifyDoc(apvDoc);
    int apvResult = 0;
    int refResult = 0;
    logger.info("getDOC_STTUS : " + apvDoc.getDOC_SN());
    //결재자
    if (!apvMbrId.equals("apvMbrId")) { //결재자 변경
      adminService.deleteApv(apvDoc.getDOC_SN());
      String[] apvArray = apvMbrId.split(",");
      for (int i = 0; i < apvArray.length; i++) {
        apv.setDOC_SN(apvDoc.getDOC_SN());
        apv.setMBR_ID(apvArray[i]);
        apv.setAPV_LEV(i + 1);
        if (param.equals("Doc")) {
          if (i == 0) {
            apv.setAPV_STTUS(0); //대기
            sseService.sendNotification(apv.getMBR_ID(), mbrNcNm+ "님이 올린 문서 결재 요청이 있습니다.", "admin/apv/apvList", 5);
          } else {
            apv.setAPV_STTUS(1); //예정
          }
        } else if (param.equals("Temp")) {
          apv.setAPV_STTUS(5); //임시
        }
        apvResult = adminService.insertApv(apv);

      }
    } else { //결재자 변경 안함
      if (param.equals("Doc")) {
        List<Approval> apvList = adminService.getApvDetail(apvDoc.getDOC_SN());
        logger.info("문서 수정 (문서 getDOC_SN) : " + apvDoc.getDOC_SN());
        if (!apvList.isEmpty()) {
          for (int i = 0; i < apvList.size(); i++) {
            apv.setMBR_ID(apvList.get(i).getMBR_ID());
            apv.setAPV_LEV(apvList.get(i).getAPV_LEV());
            if (i == 0) {
              apv.setAPV_STTUS(0); //대기
              sseService.sendNotification(apv.getMBR_ID(), mbrNcNm+"님이 올린 문서의 결재 요청이 있습니다.", "admin/apv/apvList", 5);
            } else {
              apv.setAPV_STTUS(1); //예정
            }
            adminService.modifyApv(apv);
          }
        }
      }
      apvResult = 1;

    }

    //참조자
    if (!refMbrId.equals("refMbrId") && !refMbrId.equals("")) { //새로 선택
      List<ApvRef> refList = adminService.getRefDetail(apvDoc.getDOC_SN());
      if (!refList.isEmpty()) {
        adminService.deleteRef(apvDoc.getDOC_SN());
      }
      String[] refArray = refMbrId.split(",");
      for (int i = 0; i < refArray.length; i++) {
        ref.setDOC_SN(apvDoc.getDOC_SN());
        ref.setMBR_ID(refArray[i]);
        if (param.equals("Doc")) {
          ref.setREF_STTUS(0); //참조
        } else if (param.equals("Temp")) {
          ref.setREF_STTUS(1); //임시
        }
        refResult = adminService.insertRef(ref);
      }
    } else if (refMbrId.equals("refMbrId")) { //참조자 수정 안한 경우
      if (param.equals("Doc")) {
        ref.setREF_STTUS(0);
        adminService.modifyRef(ref);
      }
      refResult = 1;
    } else if (refMbrId.equals("")) { //참조자 선택 안한 경우
      List<ApvRef> refList = adminService.getRefDetail(apvDoc.getDOC_SN());
      if (!refList.isEmpty()) {
        adminService.deleteRef(apvDoc.getDOC_SN());
      }
      refResult = 1;
    }

    logger.info("update(apvMbrId): " + apvMbrId);
    logger.info("update(apvDoc): " + apvDoc);
    logger.info("문서수정 param : " + param);

    if (docResult < 1) {
      mv.addObject("message", "문서 수정 실패");
    } else if (apvResult < 1) {
      mv.addObject("message", "결재자 등록 실패");
    } else if (refResult < 1) {
      mv.addObject("message", "참조자 등록 실패");
    } else {
      if (param.equals("Doc")) {
        mv.addObject("message", "결재 요청을 완료하였습니다.");
        mv.setViewName("redirect:draftList");
      } else if (param.equals("Temp")) {
        mv.addObject("message", "임시 저장을 완료하였습니다.");
        mv.setViewName("redirect:tempList");
      }
    }

    return mv;
  }

  //결재 상태
  @RequestMapping("apv/{param}Status")
  public ModelAndView statusUpdate(ModelAndView mv,
      @PathVariable("param") String param, @RequestParam("docSn") int docSn,
      @RequestParam("type") String type,
      @RequestParam(value = "rejRsn", required = false) String rejRsn,
      @ModelAttribute ApvDoc apvDoc, @ModelAttribute Approval apv,
      @CurrentSecurityContext SecurityContext userPrincipal) {

    String id = userPrincipal.getAuthentication().getName();

    apv.setMBR_ID(id);
    apv.setDOC_SN(docSn);

    Date currentTime = new Date();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    apv.setAPV_DT(sdf.format(currentTime));

    // 추가된 디버깅 코드
    logger.info("문서 번호 DOC_SN : " + apvDoc.getDOC_SN());
    logger.info("문서 양식 타입 DOC_FORM_TY : " + apvDoc.getDOC_FORM_TY());
    logger.info("문서 작성자 아이디 MBR_ID : " + apvDoc.getMBR_ID());
    logger.info("문서 작성자 닉네임 MBR_NCNM : " + apvDoc.getMBR_NCNM());
    logger.info("문서 작성자 타입 MBR_TY : " + apvDoc.getMBR_TY());
    logger.info("문서 제목 DOC_SJ : " + apvDoc.getDOC_SJ());
    logger.info("문서 내용 DOC_CN : " + apvDoc.getDOC_CN());
    logger.info("문서 상태 DOC_STTUS : " + apvDoc.getDOC_STTUS());
    logger.info("문서 작성 날짜 DOC_WRT_DT : " + apvDoc.getDOC_WRT_DT());
    logger.info("결재 상태 APV_STTUS : " + apvDoc.getAPV_STTUS());

    logger.info("문서 작성자 아이디 :" + apvDoc.getMBR_ID());
    logger.info("문서 DOC_SN :" + docSn);
    int apvResult = 0;
    int docResult = 0;

    String mbrId = adminService.getMbrId(docSn);
    String mbrNcNm = adminService.getMbrNCNM(docSn);
    String ApvMbrNcnm = adminService.getApvMbrNcnm(apv.getMBR_ID());

    if (param.equals("apv")) { //승인
      List<Approval> apvList = adminService.getApvStatusList(docSn);
      if (!apvList.isEmpty()) {
        adminService.modifyApvNext(apvList.get(0).getAPV_SN());
        sseService.sendNotification(apvList.get(0).getMBR_ID(), mbrNcNm+"님이 올린 문서의 결재 차례가 되었습니다.",
            "admin/apv/apvList", 5);
        apv.setAPV_STTUS(3); //완료
        apv.setDOC_STTUS("1"); //진행
      } else {
        apv.setAPV_STTUS(3); //완료
        apv.setDOC_STTUS("2"); //완료
        sseService.sendNotification(mbrId,"문서가 결재 완료되었습니다.","admin/apv/draftList",5);
      }
    } else if (param.equals("rej")) { //반려
      apv.setREJ_RSN(rejRsn);
      apv.setAPV_STTUS(4); //반려
      apv.setDOC_STTUS("3"); //반려
      sseService.sendNotification(mbrId,ApvMbrNcnm+"님이 문서를 반려하였습니다.","admin/apv/draftList",5);
    }
    apvResult = adminService.modifyApvStatus(apv);
    logger.info(String.valueOf(apvResult));
    docResult = adminService.modifyDocStatus(apv);
    logger.info(String.valueOf(docResult));
    String docSttus = apv.getDOC_STTUS();

    logger.info("문서 상태 docSttus = " + docSttus);

    if (apvResult > 0 && docResult > 0) {
      mv.setViewName(
          "redirect:./docDetail?docSn=" + docSn + "&type=" + type + "&docSttus=" + docSttus);
    } else {
      if (param.equals("apv")) {
        mv.addObject("message", "승인 실패");
        logger.info("승인 실패");
      } else if (param.equals("rej")) {
        mv.addObject("message", "반려 실패");
        logger.info("반려 실패");
      }
      mv.addObject("loc", "docDetail?docSn=" + docSn + "&type=" + type + "&docSttus=0");
    }
    return mv;
  }


}
