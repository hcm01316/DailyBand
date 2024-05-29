package com.bnd.dailyband.mybatis.mapper;

import com.bnd.dailyband.domain.Member;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AdminMapper {
  int getSearchListCount(Map<String, String> map);

  List<Member> getSearchList(Map<String, Object> map);
}
