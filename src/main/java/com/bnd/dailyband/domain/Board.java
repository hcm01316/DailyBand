package com.bnd.dailyband.domain;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class Board {
    private int BBS_SN;
    private String BBS_TY_ID;
    private String MBR_ID;
    private String BBS_SJ;
    private String BBS_CN;
    private int BBS_REMD_CNT;
    private int BBS_NREMD_CNT;
    private int BBS_READ_CNT;
    private String GENRE_ID;
    private String YT_ADDR;
    private Timestamp REG_DT;
    private Timestamp MDF_DT;
    private String MBR_NCNM;
    private String MBR_PROFL_PHOTO;
    private String CTGRY_NM;
    private String COMMENT_COUNT;

    public String get_GENRE_ID() {
        String genre = GENRE_ID;
        if (genre.equals("G01")) {
            genre = "팝";
        } else if (genre.equals("G02")) {
            genre = "발라드";
        } else if (genre.equals("G03")) {
            genre = "인디음악";
        } else if (genre.equals("G04")) {
            genre = "랩·힙합";
        } else if (genre.equals("G05")) {
            genre = "K-POP";
        } else if (genre.equals("G06")) {
            genre = "트로트";
        } else if (genre.equals("G07")) {
            genre = "일렉트로닉";
        } else if (genre.equals("G08")) {
            genre = "락";
        } else if (genre.equals("G09")) {
            genre = "메탈";
        } else if (genre.equals("G10")) {
            genre = "R&B";
        } else if (genre.equals("G11")) {
            genre = "재즈";
        } else if (genre.equals("G12")) {
            genre = "클래식";
        } else if (genre.equals("G13")) {
            genre = "뮤지컬";
        } else if (genre.equals("G14")) {
            genre = "국악";
        } else if (genre.equals("G15")) {
            genre = "J-POP";
        } else if (genre.equals("G16")) {
            genre = "월드뮤직";
        }
        return genre;
    }

}
