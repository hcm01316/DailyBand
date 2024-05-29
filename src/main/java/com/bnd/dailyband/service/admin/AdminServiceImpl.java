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
  public int getSearchListCount(int index, String searchWord) {
    Map<String, String> map = new HashMap<>();
    if(index != -1) {
      String[] search_field = new String[] {"id", "name", "age", "gender"};
      map.put("search_field", search_field[index]);
      map.put("search_word", "%" + searchWord + "%");
    }
    return adao.getSearchListCount(map);
  }

  @Override
  public List<Member> getSearchList(int index, String searchWord, int page, int limit) {
    Map<String, Object> map = new HashMap<>();

    //http://localhost:9000/dailyband/admin/mbrmgmt 로 접속하는 경우
    //select를 선택하지 않아 index는 "-1"의 값을 갖습니다.
    //이 경우 아래의 문장을 수행하지 않기 때문에 "search_field" 키에 대한 map.get("search_field")의 값은 null
    if(index != -1) {
      String[] search_field = new String[] {"id", "name", "age", "gender"};
      map.put("search_field", search_field[index]);
      map.put("search_word", "%" + searchWord + "%");
    }
    int startrow = (page-1) * limit + 1;
    int endrow = startrow + limit - 1;
    map.put("start", startrow);
    map.put("end", endrow);
    return adao.getSearchList(map);
  }
}
