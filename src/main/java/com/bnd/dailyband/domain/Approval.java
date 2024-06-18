package com.bnd.dailyband.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
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
  private String DOC_STTUS;

}
