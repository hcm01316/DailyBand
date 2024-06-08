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
        case "G01":
          genre.append("팝 ");
          break;
        case "G02":
          genre.append("발라드 ");
          break;
        case "G03":
          genre.append("인디음악 ");
          break;
        case "G04":
          genre.append("랩·힙합 ");
          break;
        case "G05":
          genre.append("K-POP ");
          break;
        case "G06":
          genre.append("트로트 ");
          break;
        case "G07":
          genre.append("일렉트로닉 ");
          break;
        case "G08":
          genre.append("락 ");
          break;
        case "G09":
          genre.append("메탈 ");
          break;
        case "G10":
          genre.append("R&B ");
          break;
        case "G11":
          genre.append("재즈 ");
          break;
        case "G12":
          genre.append("클래식 ");
          break;
        case "G13":
          genre.append("뮤지컬 ");
          break;
        case "G14":
          genre.append("국악 ");
          break;
        case "G15":
          genre.append("J-POP ");
          break;
        case "G16":
          genre.append("월드뮤직 ");
          break;
      }
    }

    return genre.toString().trim();
  }

  public String getMBR_PREFER_AREAK() {
    if (MBR_PREFER_AREA == null) {
      return "";
    }

    String[] aresult = MBR_PREFER_AREA.split(",");
    StringBuilder area = new StringBuilder();

    for (String a : aresult) {
      switch (a) {
        case "A01":
          area.append("서울");
          break;
        case "A02":
          area.append("경기");
          break;
        case "A03":
          area.append("부산");
          break;
        case "A04":
          area.append("대구");
          break;
        case "A05":
          area.append("광주");
          break;
        case "A06":
          area.append("대전");
          break;
        case "A07":
          area.append("충남");
          break;
        case "A08":
          area.append("충북");
          break;
        case "A09":
          area.append("세종");
          break;
        case "A10":
          area.append("울산");
          break;
        case "A11":
          area.append("인천");
          break;
        case "A12":
          area.append("강원");
          break;
        case "A13":
          area.append("전남");
          break;
        case "A14":
          area.append("전북");
          break;
        case "A15":
          area.append("경북");
          break;
        case "A16":
          area.append("경남");
          break;
        case "A17":
          area.append("제주");
          break;
      }
    }
    return area.toString().trim();
  }

  public String getMBR_ACT_REALMK() {
    if (MBR_ACT_REALM == null) {
      return "";
    }

    String[] gresult = MBR_ACT_REALM.split(",");
    StringBuilder realm = new StringBuilder();

    for (String a : gresult) {
      switch (a) {
        case "R01":
          realm.append("기타/베이스 ");
          break;
        case "R02":
          realm.append("드럼 ");
          break;
        case "R03":
          realm.append("키보드 ");
          break;
        case "R04":
          realm.append("보컬 ");
          break;
        case "R05":
          realm.append("그 외 ");
          break;
      }
    }

    return realm.toString().trim();
  }

}
