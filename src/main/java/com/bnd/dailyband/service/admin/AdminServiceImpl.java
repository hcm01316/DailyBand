package com.bnd.dailyband.service.admin;

import com.bnd.dailyband.domain.Approval;
import com.bnd.dailyband.domain.ApvDoc;
import com.bnd.dailyband.domain.ApvRef;
import com.bnd.dailyband.domain.Member;
import com.bnd.dailyband.mybatis.mapper.AdminMapper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {

  private AdminMapper adao;

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

  //기안 문서 작성 process (결재자 등록)
  @Override
  public int processApvList(String apvMbrId, String param, int docSn) {
    int apvResult = 0;
    if (!apvMbrId.equals("apvMbrId") || param.equals("Temp") || param.equals("Doc")) {
      String[] apvArray = apvMbrId.split(",");
      for (int i = 0; i < apvArray.length; i++) {
        Approval apv = new Approval();
        apv.setDOC_SN(docSn);
        apv.setMBR_ID(apvArray[i]);
        apv.setAPV_LEV(i + 1);
        apv.setAPV_STTUS(param.equals("Temp") ? 5 : (i == 0 ? 0 : 1)); //5는 임시
        apvResult += insertApv(apv); // 결재자 등록
      }
    } else { // 반려된 문서 재상신 - 결재자 선택 안함
      List<Approval> apvList = getApvDetail(docSn);
      for (int i = 0; i < apvList.size(); i++) {
        Approval existingApv = apvList.get(i);
        existingApv.setAPV_STTUS(param.equals("RejTemp") ? 5 : (i == 0 ? 0 : 1));
        apvResult += insertApv(existingApv);
      }
    }
    return apvResult;
  }

  //기안 문서 작성 process (참조자 등록)
  @Override
  public int processRefList(String refMbrId, String param, int docSn) {
    int refResult = 0;
    if (param.equals("RejDoc") || param.equals("RejTemp")) { // 재상신
      if (refMbrId.equals("refMbrId")) {
        List<ApvRef> refList = getRefDetail(docSn);
        for (ApvRef existingRef : refList) {
          existingRef.setREF_STTUS(param.equals("RejTemp") ? 1 : 0);
          refResult += insertRef(existingRef);
        }
        if (refList.isEmpty()) {
          refResult = 1;
        }
      } else if (!refMbrId.isEmpty()) {
        String[] refArray = refMbrId.split(",");
        for (String refMemberId : refArray) {
          ApvRef newRef = new ApvRef();
          newRef.setDOC_SN(docSn);
          newRef.setMBR_ID(refMemberId);
          newRef.setREF_STTUS(param.equals("RejTemp") ? 1 : 0);
          refResult += insertRef(newRef);
        }
      } else { // refMbrId가 비어있을 경우
        refResult = 1;
      }
    } else { // 재상신이 아닌 경우
      if (!refMbrId.isEmpty()) {
        String[] refArray = refMbrId.split(",");
        for (String refMemberId : refArray) {
          ApvRef newRef = new ApvRef();
          newRef.setDOC_SN(docSn);
          newRef.setMBR_ID(refMemberId);
          newRef.setREF_STTUS(param.equals("Temp") ? 1 : 0);
          refResult += insertRef(newRef);
        }
      } else { // refMbrId가 비어있을 경우
        refResult = 1;
      }
    }
    return refResult;
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


}
