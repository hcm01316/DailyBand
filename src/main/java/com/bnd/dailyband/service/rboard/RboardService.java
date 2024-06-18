package com.bnd.dailyband.service.rboard;

import com.bnd.dailyband.domain.Ctgry;
import com.bnd.dailyband.domain.Bandhr;
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
    public List<Bandhr>getbandList(int bbs_sn);

    // 밴드 가입 여부 확인1
    public int bandck(String id);

    // 리더 여부 확인
    public int leaderck(String id);

    // 밴드 가입 수락
    public int bandaccept(String id,int num);

    // 밴드 가입 여부 확인2
    public int isjoin(String id);

    // 밴드 가입 신청
    public void join(String id, int num);

    // 가입 수락시 다른 가입 대기중 삭제
    public void joinwatingdel(int num);

    public List<Bandhr>getjoinlist(int bbs_sn);
    // 자신이 속한 밴드 확인
    public int myband (String id);

    // 밴드 강퇴
    public int resign(String id, int num);

    // 밴드 거절
    public int refuse(String id, int num);

    // 밴드 해체
    public int breakup(int num);

    // 게시글 작성시 밴드 참여정보에도 리더 추가
    public void insertBand(String id, int num);

    // 방금 작성한 게시글 번호 불러오기
    public int getaddnum();

    // 밴드 탈퇴
    public int leave(String id, int num);

    // 현재 모집한 인원수
    public int bandacceptcnt(int num);
    // 모집 인원수
    public int getrenope(int num);

    public void teamstclose(int num);

    public void teamstopen(int num);

    public int updateRboard(Rboard rboard);

    public void BandChatRoomCreate(String chatname, int hc);

    public int getChatNum();

    public String BandTeamName(int num);

    public int MyBandChat(String name);

    public void BandChatJoin(int chat, String id);

    public int JoinCk(int num, String id);

    public int JoinCancel(int num, String id);

    public String getleader(int num);

    public List<String> bandlist(int num);
}