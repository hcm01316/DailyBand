package com.bnd.dailyband.mybatis.mapper;

import com.bnd.dailyband.domain.Ctgry;
import com.bnd.dailyband.domain.Bandhr;
import com.bnd.dailyband.domain.Rboard;
import com.bnd.dailyband.domain.Rlist;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Mapper
public interface RboardMapper  {

    // 글의 갯수 구하기
    public int getListCount();

    // 모집 게시판 리시트 구하기
    public List<Rlist> getRboardList(HashMap<String, Integer> map);

    // 모집 게시판 글 내용 보기
    public Rboard getDetail(int num);

    //글 수정
    public int boardModify(Rboard modifyboard);

    //글 삭제
    public int boardDelete(Rboard rboard);

    // 조회수 업데이트
    public int setReadCountUpdate(int num);

    // 글쓴이인지 확인
    public Rboard isBoardWriter(HashMap<String, Object> map);

    // 글 등록하기
    public void insertRboard(Rboard rboard);

    // 밴드 카테고리 리스트
    public ArrayList<Ctgry> getCtgryList(int type);

    // 밴드 회원 리스트
    public List<Bandhr> getbandList(int bbs_sn);

    //밴드 가입 여부 확인
    public Rboard bandck(String id);

    // 밴드 리더 체크
    public int leaderck(String id);

    // 밴드 가입 수락
    public int bandaccept(String id,int num);

    //밴드 가입 여부 확인
    public int isjoin(String id);

    //밴드 가입 신청
    public void join(String id, int num);

    //가입 수락시 다른 신청 대기중 삭제
    public void joinwatingdel(int num);

    //가입 대기 리스트
    public List<Bandhr>getjoinlist(int bbs_sn);

    // 자신이 속한 밴드 확인
    public int myband (String id);

    // 밴드원 강퇴
    public int resign(String id, int num);

    // 밴드원 거절
    public int refuse(String id, int num);

    // 밴드 해체
    public int breakup(int num);

    // 게시글 작성시 밴드모집 테이블에도 추가
    public void insertBand(String id, int num);

    // 방금 작성한 게시글 번호
    public int getaddnum();

    // 밴드 탈퇴
    public int leave(String id, int num);

    // 현재 모집한 인원수
    public int bandacceptcnt(int num);

    public int getrenope(int num);

    // 모집중 > 모집완료 변경
    public void teamstclose(int num);

    public void teamstopen(int num);

    public int updateRboard(Rboard rboard);

    public void BandChatRoomCreate(String chatname,int hc);

    public int getChatNum();

    public String BandTeamName(int num);

    public int MyBandChat(String name);

    public void BandChatJoin(int chat,String id);

    public void ChatLeave(int chat, String id);

    public int JoinCk(int num, String id);

    public int JoinCancel(int num, String id);

    public String getleader(int num);

    public List<String> bandlist(int num);
}