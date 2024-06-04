package com.bnd.dailyband.domain;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Bandhr {

  private String MBR_ID;

  private String BBS_SN;
  private String MBR_NCNM;
  private int MBR_AGE;
  private String MBR_GENDER;
  private String MBR_PREFER_AREA;

  private String MBR_ACT_REALM;
  private String MBR_PREFER_GENRE;
  private String MBR_PROFL_PHOTO;
  private int MBR_PRPT_STTUS;

  public String getMBR_PREFER_GENREK() {
    if (MBR_PREFER_GENRE == null) {
      return "";
    }

    String[] gresult = MBR_PREFER_GENRE.split(",");
    StringBuilder genre = new StringBuilder();

    for (String a : gresult) {
      switch (a) {
        case "G01": genre.append("팝 "); break;
        case "G02": genre.append("발라드 "); break;
        case "G03": genre.append("인디음악 "); break;
        case "G04": genre.append("랩·힙합 "); break;
        case "G05": genre.append("K-POP "); break;
        case "G06": genre.append("트로트 "); break;
        case "G07": genre.append("일렉트로닉 "); break;
        case "G08": genre.append("락 "); break;
        case "G09": genre.append("메탈 "); break;
        case "G10": genre.append("R&B "); break;
        case "G11": genre.append("재즈 "); break;
        case "G12": genre.append("클래식 "); break;
        case "G13": genre.append("뮤지컬 "); break;
        case "G14": genre.append("국악 "); break;
        case "G15": genre.append("J-POP "); break;
        case "G16": genre.append("월드뮤직 "); break;
      }
    }

    return genre.toString().trim();
  }

  public String getMBR_PREFER_AREAK() {
    if (MBR_PREFER_AREA == null) {
      return "";
    }

    switch (MBR_PREFER_AREA) {
      case "A01": return "서울";
      case "A02": return "경기";
      case "A03": return "부산";
      case "A04": return "대구";
      case "A05": return "광주";
      case "A06": return "대전";
      case "A07": return "충남";
      case "A08": return "충북";
      case "A09": return "세종";
      case "A10": return "울산";
      case "A11": return "인천";
      case "A12": return "강원";
      case "A13": return "전남";
      case "A14": return "전북";
      case "A15": return "경북";
      case "A16": return "광남";
      case "A17": return "제주";
      default: return "";
    }
  }

  public String getMBR_ACT_REALMK() {
    if (MBR_ACT_REALM == null) {
      return "";
    }

    String[] gresult = MBR_ACT_REALM.split(",");
    StringBuilder realm = new StringBuilder();

    for (String a : gresult) {
      switch (a) {
        case "R01": realm.append("기타/베이스 "); break;
        case "R02": realm.append("드럼 "); break;
        case "R03": realm.append("키보드 "); break;
        case "R04": realm.append("보컬 "); break;
        case "R05": realm.append("그 외 "); break;
      }
    }

    return realm.toString().trim();
  }

}
