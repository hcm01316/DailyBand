package com.bnd.dailyband.service.admin;

import com.bnd.dailyband.domain.Member;
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

  @Override
  public List<Member> getMemberList() {
    Map<String, Object> map = new HashMap<>();
    return adao.getMemberList(map);
  }

  @Override
  public void memberTY(String id) {
    adao.memberTY(id);
  }

  @Override
  public void managerTY(String id) {
    adao.managerTY(id);
  }

  @Override
  public void adminTY(String id) {
    adao.adminTY(id);
  }

  @Override
  public void memberStatusAc(String id) {
    adao.memberStatusAc(id);
  }

  @Override
  public void memberStatusIn(String id) {
    adao.memberStatusIn(id);
  }

  @Override
  public int getListCount() {
    return adao.getListCount();
  }


}
