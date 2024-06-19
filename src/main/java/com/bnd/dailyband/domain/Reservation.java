package com.bnd.dailyband.domain;

import lombok.Data;

import java.util.Date;

@Data
public class Reservation {
    private int cal_id;
    private int bbs_sn;
    private String title;
    private String start;
    private String end;
    private String color;
    private String textColor;
    private Date reg_dt;
    private int room_number; // 방 번호
    private String status; // 상태 (대기, 수락, 거절)
}
