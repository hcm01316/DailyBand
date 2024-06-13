package com.bnd.dailyband.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Approval {
  private int APV_SN;
  private int DOC_SN;
  private String MBR_ID;
  private String MBR_NCNM;
  private String MBR_TY;
  private int APV_LEV;
  private int APV_STTUS;
  private String REJ_RSN;
  private String APV_DT;
  private int DOC_STTUS;

  @Override
  public String toString() {
    return "Approval{" +
        "APV_SN='" + APV_SN + '\'' +
        ", DOC_SN='" + DOC_SN + '\'' +
        ", MBR_ID='" + MBR_ID + '\'' +
        ", MBR_NCNM='" + MBR_NCNM + '\'' +
        ", MBR_TY='" + MBR_TY + '\'' +
        ", APV_LEV=" + APV_LEV +
        ", APV_STTUS='" + APV_STTUS + '\'' +
        ", REJ_RSN='" + REJ_RSN + '\'' +
        ", APV_DT='" + APV_DT + '\'' +
        ", DOC_STTUS='" + DOC_STTUS + '\'' +
        '}';
  }
}
