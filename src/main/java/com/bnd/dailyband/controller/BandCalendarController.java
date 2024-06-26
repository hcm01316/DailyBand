package com.bnd.dailyband.controller;

import com.bnd.dailyband.domain.Calendar;
import com.bnd.dailyband.domain.Member;
import com.bnd.dailyband.domain.Rboard;
import com.bnd.dailyband.service.Calendar.CalendarService;
import com.bnd.dailyband.service.admin.AdminService;
import com.bnd.dailyband.service.rboard.RboardService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@Slf4j
@RequestMapping("/calendar")
public class BandCalendarController {

    @Autowired
    private CalendarService calendarService;
    private AdminService adminService;

    public BandCalendarController(CalendarService calendarService,
        AdminService adminService){
        this.calendarService = calendarService;
        this.adminService = adminService;
    }

    private static final Logger logger = LoggerFactory.getLogger(BandCalendarController.class);

    @GetMapping("/bandcal")
    public ModelAndView showBandCalendarPage(ModelAndView mv, @CurrentSecurityContext SecurityContext userPrincipal) {

        String id = userPrincipal.getAuthentication().getName();
        mv.addObject("id", id);

        Rboard rboard = calendarService.bandck(id);

        int bandck = 0;
        if(rboard == null){
            bandck = -1;
        }
        mv.addObject("bandck", bandck);

        if (bandck == -1) {
            mv.addObject("isband", 0);
        }
        if (bandck != -1)
        {
            mv.addObject("bbsSn",rboard.getBBS_SN());
        }
        mv.addObject("current", "bandCal");
        mv.setViewName("calendar/band_calendar");

        return mv;
    }

    @GetMapping("/select")
    @ResponseBody
    public List<Calendar> select(int bbsSn){

        return calendarService.getAllCalendars(bbsSn);

    }

    // 모든 요청에 대해 사용자 정보를 모델에 추가
    @ModelAttribute
    public void addAttributes(Model model, @AuthenticationPrincipal Member member) {
        int resCnt = adminService.resWaitCnt();
        model.addAttribute("resCnt", resCnt);
        if (member != null) {
            model.addAttribute("profilePhoto", member.getProfilePhoto());
            model.addAttribute("username", member.getUsername());
        }
    }

    @PostMapping("/add")
    @ResponseBody
    public String addCalendar(Calendar calendar) {
        log.info(calendar.toString());
        calendarService.addCalendar(calendar);
        return "ok";
    }

    @PostMapping("/delete/{cal_id}")
    @ResponseBody
    public String deleteCalendar(@PathVariable("cal_id") int cal_id) {
        calendarService.deleteCalendar(cal_id);
        return "ok";
    }

    @PostMapping("/update")
    @ResponseBody
    public String updateCalendar(@RequestBody Calendar calendar) {
        log.info(calendar.toString());
        calendarService.updateCalendar(calendar);
        return "ok";
    }

}
