package com.bnd.dailyband.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApvDoc {
  private int DOC_SN;
  private int DOC_FORM_TY;
  private String MBR_ID;
  private String MBR_NCNM;
  private String DOC_SJ;
  private String DOC_CN;
  private int DOC_STTUS;
  private String DOC_WRT_DT;
  private int APV_STTUS;

  @Override
  public String toString() {
    return "ApvDoc{" +
        "DOC_SN=" + DOC_SN +
        ", DOC_FORM_TY=" + DOC_FORM_TY +
        ", MBR_ID='" + MBR_ID + '\'' +
        ", MBR_NCNM='" + MBR_NCNM + '\'' +
        ", DOC_SJ='" + DOC_SJ + '\'' +
        ", DOC_CN='" + DOC_CN + '\'' +
        ", DOC_STTUS=" + DOC_STTUS +
        ", DOC_WRT_DT='" + DOC_WRT_DT + '\'' +
        ", APV_STTUS='" + APV_STTUS + '\'' +
        '}';
  }
}
