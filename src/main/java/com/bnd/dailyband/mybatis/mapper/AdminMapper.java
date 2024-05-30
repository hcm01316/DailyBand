package com.bnd.dailyband.mybatis.mapper;

import com.bnd.dailyband.domain.Member;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AdminMapper {

  List<Member> getMemberList(Map<String, Object> map);

  void memberTY(String id);

  void managerTY(String id);

  void adminTY(String id);

  void memberStatusAc(String id);

  void memberStatusIn(String id);
}
