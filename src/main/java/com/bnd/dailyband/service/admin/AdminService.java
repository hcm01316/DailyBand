package com.bnd.dailyband.service.admin;

import com.bnd.dailyband.domain.Approval;
import com.bnd.dailyband.domain.ApvDoc;
import com.bnd.dailyband.domain.ApvRef;
import com.bnd.dailyband.domain.Member;
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

  //기안 문서 리스트
  List<ApvDoc> getApvDraftList(String id);

  //임시 문서 리스트
  List<ApvDoc> getApvTempList(String id);

  //결재 문서 리스트
  List<ApvDoc> getApvList(String id, String docSttus);

  //참조 문서 리스트
  List<ApvDoc> getApvRefList(String id, String docSttus);

  //결재자/참조자 회원 리스트
  List<Member> getApvMbrList(String id);

  //결재자/참조자 회원 닉네임 검색
  List<Member> getApvMbrNcnmSearch(String id, String searchKeyword);

  //기안문서 작성
  int insertDoc(ApvDoc apvDoc);

  //기안문서 작성(결재자 등록)
  int insertApv(Approval apv);

  //기안문서 작성(참조자 등록)
  int insertRef(ApvRef ref);

  //기안 문서 작성 process (결재자 등록)
  int processApvList(String apvMbrId, String param, int docSn);

  //기안 문서 작성 process (참조자 등록)
  int processRefList(String refMbrId, String param, int docSn);

  //문서 조회
  ApvDoc getDocDetail(int docSn);

  //문서 조회(결재자)
  List<Approval> getApvDetail(int docSn);

  //문서 조회(참조자)
  List<ApvRef> getRefDetail(int docSn);

  //문서 삭제(상신 취소)
  int deleteDoc(int docSn);

  //임시 저장 문서 수정
  int modifyDoc(ApvDoc apvDoc);

  //결재자 삭제
  void deleteApv(int docSn);

  //결재자 상태 변경 (temp - 대기/예정)
  void modifyApv(Approval apv);

  //참조자 삭제
  void deleteRef(int docSn);

  //참조자 상태 변경 (temp - 참조)
  void modifyRef(ApvRef ref);

  //결재 상태
  List<Approval> getApvStatusList(int docSn);

  //다음 결재자 상태 변경
  void modifyApvNext(int apvSn);

  //결재자 상태 변경 - 결재 승인/반려
  int modifyApvStatus(Approval apv);

  //문서 상태 변경 - 결재 승인/반려
  int modifyDocStatus(Approval apv);

}
