package com.bnd.dailyband.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class ApvRef {

  private int REF_SN;
  private int DOC_SN;
  private String MBR_ID;
  private String MBR_NCNM;
  private String MBR_TY;
  private int REF_STTUS;
  private int DOC_STTUS;
  private String REF_REG_DT;

}
