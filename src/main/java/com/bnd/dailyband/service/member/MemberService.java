package com.bnd.dailyband.service.member;

import com.bnd.dailyband.domain.Ctgry;
import com.bnd.dailyband.domain.Member;
import com.bnd.dailyband.domain.Social;
import org.springframework.web.multipart.MultipartFile;

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

	public int myinfo_modify(Member m,String id);

	public int social_insert(Social social,String id);

	public int social_update(Social social,String id);

	public int isEmail(String email);

	public void imageupdate(String url, String id);

	public Member myallinfo(String id);

	public String findIdByEmail(String email);

	public String findPassByEmail(String email);

	public int pwd_update(String memberEmail, String encPassword);
}
