package com.bnd.dailyband.controller;

import com.bnd.dailyband.domain.Member;
import com.bnd.dailyband.service.admin.AdminService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
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
  public ModelAndView memberList(ModelAndView mv) {
    mv.addObject("current", "admin");
    mv.addObject("current_show", "admin");
    mv.addObject("current_drop", "adminMbrMgmt");

    List<Member> list = adminService.getMemberList();

    mv.setViewName("admin/mbr_mgmt");
    mv.addObject("memberlist", list);
    return mv;
  }

  //회원 권한 변경
  @RequestMapping("/changeRole")
  public String changeRole(@RequestParam String id, @RequestParam String role) {
    //logger.info("아이디 : " + id + " / 새 권한 : " + role);

    switch (role) {
      case "ROLE_MEMBER":
        adminService.memberTY(id);
        break;
      case "ROLE_MANAGER":
        adminService.managerTY(id);
        break;
      case "ROLE_ADMIN":
        adminService.adminTY(id);
        break;
    }
    return "redirect:/admin/mbrmgmt";
  }

  //회원 상태 변경
  @RequestMapping("changeStatus")
  public String changeStatus(@RequestParam String id, @RequestParam int status) {
    //logger.info("회원 아이디: "+ id + "/ 선택한 회원 상태 : "+ status);
    switch (status) {
      case 0:
        adminService.memberStatusAc(id);
        break;
      case 1:
        adminService.memberStatusIn(id);
    }
    return "redirect:/admin/mbrmgmt";
  }


}
