package com.bnd.dailyband.mybatis.mapper;

import com.bnd.dailyband.domain.*;
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

    // 모집완료 > 모집중 변경
    public void teamstopen(int num);

    // 밴드원 모집 게시글 수정
    public int updateRboard(Rboard rboard);

    // 밴드원 모집 게시글 작성시 채팅방 생성
    public void BandChatRoomCreate(String chatname,int hc);

    // 방금 만든 밴드 채팅방 번호 가져오기
    public int getChatNum();

    // 해당 밴드 팀명 가져오기
    public String BandTeamName(int num);

    // 팀명으로 채팅방 번호 가져오기
    public int MyBandChat(String name);

    // 밴드 가입 수락시 밴드 채팅 초대
    public void BandChatJoin(int chat,String id);

    // 밴드 가입 확인
    public int JoinCk(int num, String id);

    // 밴드 가입 거절
    public int JoinCancel(int num, String id);

    // 리더 확인
    public String getleader(int num);

    // 밴드 리스트 가져오기
    public List<String> bandlist(int num);

    // 팀명 중복 검사
    public int isTeamName(String BAND_TEAM_NM);

    List<Reservation> getRoomResList(int num);

    // 가입대기, 가입상태 화긴
    public int writeck(String id);
}