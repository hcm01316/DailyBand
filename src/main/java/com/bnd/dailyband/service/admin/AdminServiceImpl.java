package com.bnd.dailyband.service.admin;

import com.bnd.dailyband.domain.Approval;
import com.bnd.dailyband.domain.ApvDoc;
import com.bnd.dailyband.domain.ApvRef;
import com.bnd.dailyband.domain.Member;
import com.bnd.dailyband.mybatis.mapper.AdminMapper;
import com.bnd.dailyband.service.notify.SseService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {

  private AdminMapper adao;
  private SseService sseService;

  @Autowired
  public AdminServiceImpl(AdminMapper adao) {
    this.adao = adao;
  }

  //회원 목록
  @Override
  public List<Map<String, Object>> getMemberList() {
    return adao.getMemberList();
  }

  //회원 권한 변경
  @Override
  public int changeType(String id, String role) {
    return adao.changeType(id, role);
  }

  //회원 상태 변경
  @Override
  public int changeStatus(String id, int newStatus) {
    return adao.changeStatus(id, newStatus);
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
    return map;
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

  @Override
  public int getApvWaitCnt(String id) {
    return adao.getApvWaitCnt(id);
  }

  @Override
  public int getApvScheduledCnt(String id) {
    return adao.getApvScheduledCnt(id);
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

  //커뮤니티 게시글 조회
  @Override
  public List<Map<String, Object>> getGboardList() {
    return adao.getGboardList();
  }

  //커뮤니티 댓글 조회
  @Override
  public List<Map<String, Object>> getGboardCmntList() {
    return adao.getGboardCmntList();
  }

  //커뮤니티 게시글 삭제
  @Override
  public int gBoardDelete(int num) {
    return adao.gBoardDelete(num);
  }

  //커뮤니티 댓글 삭제
  @Override
  public int gBoardCmntDelete(int num) {
    return adao.gBoardCmntDelete(num);
  }

  //합주실 예약 리스트
  @Override
  public List<Map<String, Object>> getRoomResList() {
    return adao.getRoomResList();
  }

  //합주실 예약 수락
  @Override
  public int resAccept(int num) {
    return adao.resAccept(num);
  }

  //합주실 예약 거절
  @Override
  public int resReject(int num) {
    return adao.resReject(num);
  }

  //기안 문서 리스트
  @Override
  public List<ApvDoc> getApvDraftList(String id) {
    return adao.getApvDraftList(id);
  }

  //임시 문서 리스트
  @Override
  public List<ApvDoc> getApvTempList(String id) {
    return adao.getApvTempList(id);
  }

  //결재 문서 리스트
  @Override
  public List<ApvDoc> getApvList(String id, String docSttus) {
    return adao.getApvList(id, docSttus);
  }

  //참조 문서 리스트
  @Override
  public List<ApvDoc> getApvRefList(String id, String docSttus) {
    return adao.getApvRefList(id, docSttus);
  }

  //결재자/참조자 회원 리스트
  @Override
  public List<Member> getApvMbrList(String id) {
    return adao.getApvMbrList(id);
  }

  //결재자/참조자 회원 닉네임 검색
  @Override
  public List<Member> getApvMbrNcnmSearch(String id, String searchKeyword) {
    return adao.getApvMbrNcnmSearch(id, searchKeyword);
  }

  //기안문서 작성
  @Override
  public int insertDoc(ApvDoc apvDoc) {
    return adao.insertDoc(apvDoc);
  }

  //기안문서 작성(결재자 등록)
  @Override
  public int insertApv(Approval apv) {
    return adao.insertApv(apv);
  }

  //기안문서 작성(참조자 등록)
  @Override
  public int insertRef(ApvRef ref) {
    return adao.insertRef(ref);
  }


  //문서 조회
  @Override
  public ApvDoc getDocDetail(int docSn) {
    return adao.getDocDetail(docSn);
  }

  //문서 조회(결재자)
  @Override
  public List<Approval> getApvDetail(int docSn) {
    return adao.getApvDetail(docSn);
  }

  //문서 조회(참조자)
  @Override
  public List<ApvRef> getRefDetail(int docSn) {
    return adao.getRefDetail(docSn);
  }

  //문서 삭제(상신 취소)
  @Override
  public int deleteDoc(int docSn) {
    return adao.deleteDoc(docSn);
  }

  //임시 저장 문서 수정
  @Override
  public int modifyDoc(ApvDoc apvDoc) {
    return adao.modifyDoc(apvDoc);
  }


  //결재자 삭제
  @Override
  public void deleteApv(int docSn) {
    adao.deleteApv(docSn);
  }

  //결재자 상태 변경
  @Override
  public void modifyApv(Approval apv) {
    adao.modifyApv(apv);
  }

  //참조자 삭제
  @Override
  public void deleteRef(int docSn) {
    adao.deleteRef(docSn);
  }

  //참조자 상태 변경 (temp - 참조)
  @Override
  public void modifyRef(ApvRef ref) {
    adao.modifyRef(ref);
  }

  //결재 상태
  @Override
  public List<Approval> getApvStatusList(int docSn) {
    return adao.getApvStatusList(docSn);
  }

  //다음 결재자 상태 변경
  @Override
  public void modifyApvNext(int apvSn) {
    adao.modifyApvNext(apvSn);
  }

  //결재자 상태 변경 - 결재 승인/반려
  @Override
  public int modifyApvStatus(Approval apv) {
    return adao.modifyApvStatus(apv);
  }

  //문서 상태 변경 - 결재 승인/반려
  @Override
  public int modifyDocStatus(Approval apv) {
    return adao.modifyDocStatus(apv);
  }

  @Override
  public int getDocSn(String docMbrId) {
    return adao.getDocSn(docMbrId);
  }

  @Override
  public String getMbrNCNM(int docSn) {
    return adao.getMbrNCNM(docSn);
  }

  @Override
  public String getMbrId(int docSn) {
    return adao.getMbrId(docSn);
  }

  @Override
  public String getApvMbrNcnm(String apvMbrId) {
    return adao.getApvMbrNcnm(apvMbrId);
  }

  @Override
  public int resWaitCnt() {
    return adao.resWaitCnt();
  }


}
