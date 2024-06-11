package com.bnd.dailyband.service.notify;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface SseService {

    public SseEmitter createEmitter(String userId);

    public void sendNotification(String userId, String message);


}
