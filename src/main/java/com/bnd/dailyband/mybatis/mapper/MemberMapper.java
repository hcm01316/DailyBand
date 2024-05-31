package com.bnd.dailyband.mybatis.mapper;

import com.bnd.dailyband.domain.Ctgry;
import com.bnd.dailyband.domain.Member;
import com.bnd.dailyband.domain.Social;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;

@Mapper
public interface MemberMapper {

	public int insert(Member m);

	public Member isId(String id);

	public Member isName(String name);

	public int myinfo_modify(String id);

	public Social mysocial(String id);

	public ArrayList<Ctgry> getCtgryList(int type);

	public int social_insert(Social social);

	public int social_update(Social social);
}
