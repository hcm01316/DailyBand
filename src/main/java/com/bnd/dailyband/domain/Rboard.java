package com.bnd.dailyband.domain;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Rboard {

  private int BBS_SN;
  private String MBR_ID;
  private String RCRIT_REALM_ID;
  private String RCRIT_GENRE_ID;
  private String RCRIT_AREA_ID;
  private String BAND_TEAM_NM;
  private String BBS_CN;
  private int RCRIT_STTUS_CD;
  private int RCRIT_NOPE;
  private String REG_DT;
  private String MDF_DT;
  private String BBS_IMG;
  private int BBS_READ_CNT;
  private String MBR_NCNM;
  private String MBR_PROFL_PHOTO;


  public String getRCRIT_REALM_IDK() {
    String gresult[] = RCRIT_REALM_ID.split(",");
    String realm = "";
    for (String a : gresult) {
      if (a.equals("R01")) {
        realm += "기타/베이스 ";
      } else if (a.equals("R02")) {
        realm += "드럼 ";
      } else if (a.equals("R03")) {
        realm += "키보드 ";
      } else if (a.equals("R04")) {
        realm += "보컬 ";
      } else if (a.equals("R05")) {
        realm += "그 외 ";
      }
    }
    return realm;
  }

  public String getRCRIT_GENRE_IDK() {
    String genre = RCRIT_GENRE_ID;
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

  public String getRCRIT_AREA_IDK() {
    String area = RCRIT_AREA_ID;
    if (area.equals("A01")) {
      area = "서울";
    } else if (area.equals("A02")) {
      area = "경기";
    } else if (area.equals("A03")) {
      area = "부산";
    } else if (area.equals("A04")) {
      area = "대구";
    } else if (area.equals("A05")) {
      area = "광주";
    } else if (area.equals("A06")) {
      area = "대전";
    } else if (area.equals("A07")) {
      area = "충남";
    } else if (area.equals("A08")) {
      area = "충북";
    } else if (area.equals("A09")) {
      area = "세종";
    } else if (area.equals("A10")) {
      area = "울산";
    } else if (area.equals("A11")) {
      area = "인천";
    } else if (area.equals("A12")) {
      area = "강원";
    } else if (area.equals("A13")) {
      area = "전남";
    } else if (area.equals("A14")) {
      area = "전북";
    } else if (area.equals("A15")) {
      area = "경북";
    } else if (area.equals("A16")) {
      area = "경남";
    } else if (area.equals("A17")) {
      area = "제주";
    }
    return area;
  }


  public String getFormattedRegDt() {
    if (REG_DT != null && REG_DT.length() >= 10) {
      return REG_DT.substring(0, 10); // 0번째 인덱스부터 10번째 인덱스까지 자름
    } else {
      return REG_DT; // 만약 REG_DT가 null이거나 길이가 10보다 작다면 그대로 반환
    }
  }

}
