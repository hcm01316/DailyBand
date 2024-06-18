package com.bnd.dailyband.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class ApvDoc {
  private int DOC_SN;
  private int DOC_FORM_TY;
  private String MBR_ID;
  private String MBR_NCNM;
  private String MBR_TY;
  private String DOC_SJ;
  private String DOC_CN;
  private String DOC_STTUS;
  private String DOC_WRT_DT;
  private int APV_STTUS;


}
