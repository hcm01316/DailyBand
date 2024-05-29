package com.bnd.dailyband.controller;

import com.bnd.dailyband.domain.Member;
import com.bnd.dailyband.service.admin.AdminService;
import java.util.List;
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

@Controller
@RequestMapping("/admin")
public class AdminController {

  private static final Logger logger = LoggerFactory.getLogger(MemberController.class);

  private AdminService adminService;

  @Autowired
  public AdminController(AdminService adminService) {
    this.adminService = adminService;
  }

  @ModelAttribute
  public void addAttributes(Model model, @AuthenticationPrincipal Member member) {
    if (member != null) {
      model.addAttribute("profilePhoto", member.getProfilePhoto());
      model.addAttribute("username", member.getUsername());
    }
  }

  @RequestMapping(value="/mbrmgmt")
  public ModelAndView memberList(
      @RequestParam(value = "page", defaultValue = "1") int page,
      @RequestParam(value = "limit", defaultValue = "3") int limit,
      ModelAndView mv,
      @RequestParam(value = "search_field", defaultValue = "-1") int index,
      @RequestParam(value = "search_word", defaultValue = "") String search_word
  ) {
    mv.addObject("current", "admin");
    mv.addObject("current_show", "admin");
    mv.addObject("current_drop", "adminMbrMgmt");
    int listcount = adminService.getSearchListCount(index, search_word); // 총 리스트 수를 받아옴

    List<Member> list = adminService.getSearchList(index, search_word, page, limit);

    // 총 페이지 수
    int maxpage = (listcount + limit - 1) / limit;

    // 현재 페이지에 보여줄 시작 페이지 수(1, 11, 21 등...)
    int startpage = ((page - 1) / 10) * 10 + 1;

    // 현재 페이지에 보여줄 마지막 페이지 수(10, 20, 30 등...)
    int endpage = startpage + 10 - 1;

    if (endpage > maxpage)
      endpage = maxpage;

    mv.setViewName("admin/mbr_mgmt");
    mv.addObject("page", page);
    mv.addObject("maxpage", maxpage);
    mv.addObject("startpage", startpage);
    mv.addObject("endpage", endpage);
    mv.addObject("listcount", listcount);
    mv.addObject("memberlist", list);
    mv.addObject("search_field", index);
    mv.addObject("search_word", search_word);
    return mv;
  }
}
