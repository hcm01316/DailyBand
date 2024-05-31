package com.bnd.dailyband.service.admin;

import com.bnd.dailyband.domain.Member;
import java.util.List;

public interface AdminService {

  //회원 목록
  List<Member> getMemberList();


  void memberTY(String id);

  void managerTY(String id);

  void adminTY(String id);

  void memberStatusAc(String id);

  void memberStatusIn(String id);

  int getListCount();
}
