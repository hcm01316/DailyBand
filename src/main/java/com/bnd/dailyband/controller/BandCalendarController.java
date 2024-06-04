package com.bnd.dailyband.controller;

import com.bnd.dailyband.domain.Calendar;
import com.bnd.dailyband.domain.Member;
import com.bnd.dailyband.service.Calendar.CalendarService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Slf4j
@RequestMapping("/calendar")
public class BandCalendarController {

    @Autowired
    private CalendarService calendarService;

    @GetMapping("/bandcal")
    public String showBandCalendarPage(Model model) {
        model.addAttribute("current", "bandCal");
        model.addAttribute("bbsSn",20);
        return "calendar/band_calendar";
    }

    @GetMapping("/select")
    @ResponseBody
    public List<Calendar> select(){

        return calendarService.getAllCalendars(20);

    }

    // 모든 요청에 대해 사용자 정보를 모델에 추가
    @ModelAttribute
    public void addAttributes(Model model, @AuthenticationPrincipal Member member) {
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

}
