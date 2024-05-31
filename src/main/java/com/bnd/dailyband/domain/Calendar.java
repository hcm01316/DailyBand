package com.bnd.dailyband.domain;

import lombok.Data;

import java.util.Date;

@Data
public class Calendar {
    private int cal_id;
    private int bbs_sn;
    private String title;
    private String start;
    private String end;
    private boolean allDay;
    private String color;
    private String textColor;
    private Date reg_dt;
}
