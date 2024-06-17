package com.bnd.dailyband.domain;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Notification {

    private int NOTIFY_ID;              //구분
    private String TARGET_MBR_ID;       //받는 사람의 아이디
    private String MESSAGE;             //알림의 내용
    private int CHECK_READ;             //알림 읽은 유무 (0 = 읽지 않음, 1 = 읽음)
    private int SEND_TIME;              //보낸 시간
    private String PASSING_URL;         //알림을 눌렀을때 넘어가는 주소
    private int MESSAGE_CAT;            //알림의 카테고리 (0 = 공지사항, 1 = 커뮤니티 gband, 2 = 밴드 모집 rband, 3 = 밴드 일정 calender
                                        // 4 = 합주실 room, 5 = 개발자)

    public Notification (String userId, String message, String url, int message_cat){
        this.TARGET_MBR_ID = userId;
        this.MESSAGE = message;
        this.PASSING_URL = url;
        this.MESSAGE_CAT = message_cat;
    }
}
