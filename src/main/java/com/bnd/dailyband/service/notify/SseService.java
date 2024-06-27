package com.bnd.dailyband.service.notify;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface SseService {

    public SseEmitter createEmitter(String userId);

    // 사용자에게 알림보내기
    public void sendNotification(String userId, String message, String url, int message_cat);


    //내가 쓰레기통을 눌렀을 때 지우기
    void delete(int num);
    
    //전체 지우기
    void allDelete(String name);
}
