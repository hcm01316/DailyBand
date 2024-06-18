package com.bnd.dailyband.controller;


import com.bnd.dailyband.domain.Member;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class RoomController {

    @RequestMapping(value = "room/info", method = RequestMethod.GET)
    public String info(Member member){
        return "room/info";
    }
}
