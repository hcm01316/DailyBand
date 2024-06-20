package com.bnd.dailyband.controller;

import com.bnd.dailyband.domain.Member;
import com.bnd.dailyband.domain.Rboard;
import com.bnd.dailyband.domain.Reservation;
import com.bnd.dailyband.service.reservation.ReservationService;
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
@RequestMapping("/room")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    public ReservationController(ReservationService reservationService){
        this.reservationService = reservationService;
    }

    private static final Logger logger = LoggerFactory.getLogger(ReservationController.class);

    @GetMapping("/reser")
    public ModelAndView showBandCalendarPage(ModelAndView mv, @CurrentSecurityContext SecurityContext userPrincipal) {
        mv.addObject("current", "room");
        mv.addObject("current_show", "room");
        mv.addObject("current_drop", "roomReser");

        String id = userPrincipal.getAuthentication().getName();
        mv.addObject("id", id);

        Rboard rboard = reservationService.bandck(id);

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
        mv.setViewName("room/room_reservation");

        return mv;
    }

    @GetMapping("/select")
    @ResponseBody
    public List<Reservation> select(int bbsSn){

        return reservationService.getAllCalendars(bbsSn);

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
    public String addCalendar(Reservation calendar) {
        reservationService.addCalendar(calendar);
        return "ok";
    }

    @PostMapping("/delete/{cal_id}")
    @ResponseBody
    public String deleteCalendar(@PathVariable("cal_id") int cal_id) {
        reservationService.deleteCalendar(cal_id);
        return "ok";
    }

    @PostMapping("/update")
    @ResponseBody
    public String updateCalendar(@RequestBody Reservation calendar) {
        log.info(calendar.toString());
        reservationService.updateCalendar(calendar);
        return "ok";
    }

    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public ModelAndView info(ModelAndView mv,Member member){
        mv.addObject("current", "room");
        mv.addObject("current_show", "room");
        mv.addObject("current_drop", "roomInfo");
        mv.setViewName("room/info");
        return mv;
    }

}
