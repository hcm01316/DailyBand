package com.bnd.dailyband.mybatis.mapper;

import com.bnd.dailyband.domain.Member;
import com.bnd.dailyband.domain.Rboard;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AdminMapper {

  //회원 목록
  List<Map<String, Object>> getMemberList();

  //회원 권한 변경
  int changeType(@Param("id") String id, @Param("role") String role);

  //회원 상태 변경
  int changeStatus(@Param("id") String id, @Param("newStatus")int newStatus);

  //전체 회원 수
  int getTotalMbrCount();

  //일일 회원 수
  int getTodayMbrCount();

  //방문 정보 추가
  void insertVisit();

  //전체 방문자 수
  int getVisitCount();

  //일 방문자 수
  int getTodayVisitCount();

  //지역별 회원 선호 지역 수
  List<Map<String, Object>> getPreferAreaCnt();

  //장르별 회원 선호 장르 수
  List<Map<String, Object>> getPreferGenreCnt();

  //분야별 회원 활동 분야 수
  List<Map<String, Object>> getActRealmCnt();

  //일별 가입자 수
  List<Map<String, Object>> getDayRegCnt();

  //일별 방문 수
  List<Map<String, Object>> getDayVisitCnt();

  //게시판별 게시글 수
  List<Map<String, Object>> getBbsCnt();

  //밴드원 모집 조회
  List<Map<String, Object>> getRboardList();

}
