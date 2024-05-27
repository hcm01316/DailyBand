package com.bnd.dailyband.service.member;

import com.bnd.dailyband.domain.Member;

public interface MemberService {
	
	public int isId(String id, String password);
	
	public int insert(Member m);

	public Member member_info(String id);
}
