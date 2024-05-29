package com.bnd.dailyband.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BandCalendarController {

    @GetMapping("/bandcal")
    public String showBandCalendarPage() {
        return "calendar/band_calendar";
    }
}
