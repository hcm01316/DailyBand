package com.bnd.dailyband.service.admin;

import com.bnd.dailyband.domain.Member;
import com.bnd.dailyband.domain.Rboard;
import com.bnd.dailyband.mybatis.mapper.AdminMapper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService{

  private AdminMapper adao;

  @Autowired
  public AdminServiceImpl(AdminMapper adao) {
    this.adao = adao;
  }

  //회원 목록
  @Override
  public List<Member> getMemberList() {
    Map<String, Object> map = new HashMap<>();
    return adao.getMemberList(map);
  }

  //회원 권한 변경
  @Override
  public int changeType(String id, String role) {
    return adao.changeType(id,role);
  }

  //회원 상태 변경
  @Override
  public int changeStatus(String id, int newStatus) {
    return adao.changeStatus(id,newStatus);
  }

  //전체 회원 수
  @Override
  public int getTotalMbrCount() {
    return adao.getTotalMbrCount();
  }

  //일일 가입 회원 수
  @Override
  public int getTodayMbrCount() {
    return adao.getTodayMbrCount();
  }

  //방문정보 추가
  @Override
  public void addVisit() {
    adao.insertVisit();
  }

  //전체 방문자 수
  @Override
  public int getTotalVisitCount() {
    return adao.getVisitCount();
  }

  //일일 방문자 수
  @Override
  public int getTodayVisitCount() {
    return adao.getTodayVisitCount();
  }

  //지역별 회원 선호 지역
  @Override
  public Map<String, Integer> getPreferAreaCnt() {
    List<Map<String, Object>> result = adao.getPreferAreaCnt();
    return convertToMap(result);
  }

  //장르별 회원 선호 장르
  @Override
  public Map<String, Integer> getPreferGenreCnt() {
    List<Map<String, Object>> result = adao.getPreferGenreCnt();
    return convertToMap(result);
  }

  //분야별 회원 활동 분야
  @Override
  public Map<String, Integer> getActRealmCnt() {
    List<Map<String, Object>> result = adao.getActRealmCnt();
    return convertToMap(result);
  }


  //회원 List 결과 변환
  private Map<String, Integer> convertToMap(List<Map<String, Object>> list) {
    Map<String, Integer> map = new HashMap<>();
    for (Map<String, Object> item : list) {
      map.put((String) item.get("name"), ((Number) item.get("count")).intValue());
    }
    return  map;
  }

  //일별 가입자 수
  @Override
  public List<Map<String, Object>> getDayRegCnt() {
    return adao.getDayRegCnt();
  }

  //일별 방문 수
  @Override
  public List<Map<String, Object>> getDayVisitCnt() {
    return adao.getDayVisitCnt();
  }

  //게시판별 게시글 수
  @Override
  public List<Map<String, Object>> getBbsCnt() {
    return adao.getBbsCnt();
  }

  //밴드원 모집 조회
  @Override
  public List<Map<String, Object>> getRboardList() {
    return adao.getRboardList();
  }


}
