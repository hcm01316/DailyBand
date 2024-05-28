package com.bnd.dailyband.service.rboard;

import com.bnd.dailyband.domain.Ctgry;
import com.bnd.dailyband.domain.Mgmt;
import com.bnd.dailyband.domain.Rboard;
import com.bnd.dailyband.domain.Rlist;

import java.util.ArrayList;
import java.util.List;

public interface RboardService {

    // 게시글 갯수
    public int getListCount();

    public List<Rlist> getRboardList(int page, int limit);

    // 카테고리 출력
    public ArrayList<Ctgry> getCtgryList(int type);

    // 게시글 작성
    public void insertRboard(Rboard rboard);

    // 조회수 1 증가
    public int setReadCountUpdate(int num);

    // 글 내용 보기
    public Rboard getDetail(int num);

    // 가입 밴드 조회
    public List<Mgmt>getbandList(int bbs_sn);

    // 밴드 가입 여부 확인
    public int bandck(String id);

    // 리더 여부 확인
    public int leaderck(String id);

    // 밴드 가입 수락
    public void bandaccept(String id);
}
