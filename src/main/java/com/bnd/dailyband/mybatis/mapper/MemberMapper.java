package com.bnd.dailyband.mybatis.mapper;

import com.bnd.dailyband.domain.Member;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberMapper {

	public int insert(Member m);

	public Member isId(String id);

	public Member isName(String name);


}
