package com.bnd.dailyband.service.admin;

import com.bnd.dailyband.domain.Member;
import com.bnd.dailyband.domain.Rboard;
import java.util.List;
import java.util.Map;

public interface AdminService {

  //회원 목록
  List<Map<String, Object>> getMemberList();


  //회원 권한 변경
  int changeType(String id, String role);

  //회원 상태 변경
  int changeStatus(String id, int newStatus);


  //전체 회원 수
  int getTotalMbrCount();

  //일일 가입 회원 수
  int getTodayMbrCount();


  //방문정보 추가
  void addVisit();

  //전체 방문자 수
  int getTotalVisitCount();

  //일일 방문자 수
  int getTodayVisitCount();


  //지역별 회원 선호 지역 수
  Map<String, Integer> getPreferAreaCnt();

  //장르별 회원 선호 장르 수
  Map<String, Integer> getPreferGenreCnt();

  //분야별 회원 활동 분야 수
  Map<String, Integer> getActRealmCnt();


  //일별 가입자 수
  List<Map<String, Object>> getDayRegCnt();

  //일별 방문 수
  List<Map<String, Object>> getDayVisitCnt();

  //게시판별 게시글 수
  List<Map<String, Object>> getBbsCnt();

  //밴드원 모집 조회
  List<Map<String, Object>> getRboardList();
}
