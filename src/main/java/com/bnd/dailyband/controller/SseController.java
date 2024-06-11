package com.bnd.dailyband.controller;

import com.bnd.dailyband.service.notify.SseService;
import org.springframework.beans.factory.annotation.Autowired;
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
}