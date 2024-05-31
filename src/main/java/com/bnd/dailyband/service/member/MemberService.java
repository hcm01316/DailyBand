package com.bnd.dailyband.service.member;

import com.bnd.dailyband.domain.Ctgry;
import com.bnd.dailyband.domain.Member;
import com.bnd.dailyband.domain.Social;

import java.io.StringReader;
import java.util.ArrayList;

public interface MemberService {
	
	public int isId(String id, String password);
	
	public int insert(Member m);

	public Member member_info(String id);

	public int isId(String id);

	public int isName(String name);

	public Social mysocial(String id);

	public ArrayList<Ctgry> getCtgryList(int type);

	public int myinfo_modify(String id);

	public int social_insert(Social social);

	public int social_update(Social social);

	int isEmail(String email);
}
