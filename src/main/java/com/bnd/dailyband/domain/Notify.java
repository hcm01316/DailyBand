package com.bnd.dailyband.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Notify {

    private String NOT_ID;          //알림 아이디
    private String MER_ID;          //멤버 아이디
    private String NOT_START_AT;    //알림이 온 시간
    private String NOT_MESSAGE;     //알림의 내용
    private int NOT_END_AT;         //알림 읽은 유무
    private String NOT_URL;         //알림의 주소
    private String TARGET_ID;       //누구에게 보낼건지
    private String NOT_READ_AT;     //알림을 읽은 시간
    private String NOT_TITLE;       //알림의 제목
}
