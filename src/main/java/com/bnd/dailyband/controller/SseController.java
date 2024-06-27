package com.bnd.dailyband.controller;

import com.bnd.dailyband.service.notify.SseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.security.Principal;


@RestController
public class SseController {
    @Autowired
    private SseService sseService;

    @GetMapping("/sse")
    public SseEmitter streamSse(Principal principal) {
        return sseService.createEmitter(principal.getName());
    }

    @DeleteMapping("/delete")
    public String MessageDelete(int num){
        sseService.delete(num);
        return "삭제 성공";
    }

    //전체를 한번에 지우기
    @DeleteMapping("/deleteAll")
    public String MessageDelete(Principal userPrincipal){
        sseService.allDelete(userPrincipal.getName());
        return "전부 삭제 성공";
    }


    // int[] FixauthUser = 권한가진 사용자 찾는 서비스 연결
}