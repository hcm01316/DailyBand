package com.bnd.dailyband.domain;

import lombok.Getter;
import lombok.Setter;
import java.sql.Timestamp;

@Getter
@Setter
public class Comment {
    private int CMNT_SN;
    private String BBS_TY_ID;
    private int BBS_SN;
    private String MBR_ID;
    private String CMNT_CN;
    private int CMNT_LEV;
    private int CMNT_SEQ;
    private int CMNT_REF;
    private int CMNT_REMD_CNT;
    private int CMNT_NREMD_CNT;
    private Timestamp REG_DT;
    private Timestamp MDF_DT;
    private String MBR_NCNM;
    private String MBR_PROFL_PHOTO;
}
