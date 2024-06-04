package com.bnd.dailyband.mybatis.mapper;

import com.bnd.dailyband.domain.Ctgry;
import com.bnd.dailyband.domain.Member;
import com.bnd.dailyband.domain.Social;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.Map;

@Mapper
public interface MemberMapper {

	public int insert(Member m);

	public Member isId(String id);

	public Member isName(String name);

	public int myinfo_modify(Map<String, Object> map);

	public Social mysocial(String id);

	public ArrayList<Ctgry> getCtgryList(int type);

	public int social_insert(Map<String, Object> map);

	public int social_update(Map<String, Object> map);

	public Member isEmail(String email);

	public void imageupdate(String url, String id);

	public Member myallinfo(String id);

	public String findIdByEmail(String email);

}
