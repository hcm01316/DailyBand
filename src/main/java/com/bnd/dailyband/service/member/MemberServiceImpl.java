package com.bnd.dailyband.service.member;

import com.bnd.dailyband.domain.Ctgry;
import com.bnd.dailyband.domain.Member;
import com.bnd.dailyband.domain.Social;
import com.bnd.dailyband.mybatis.mapper.MemberMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class MemberServiceImpl implements MemberService{

	private MemberMapper dao;
	private PasswordEncoder passwordEncoder;

	@Autowired
	public MemberServiceImpl(MemberMapper dao, PasswordEncoder passwordEncoder) {
		this.dao = dao;
		this.passwordEncoder=passwordEncoder;
	}

	@Override
	public int isId(String id, String password) {
		Member rmember = dao.isId(id);
		int result = -1; //아이디가 존재하지 않는 경우 - rmember가 null인 경우

		if(rmember != null) {	//아이디가 존재하는 경우
			//passwordEncoder.matches(rawPassword, encodedPassword)
			//사용자에게 입력받은 패스워드를 비교하고자 할 떄 사용하는 메서드입니다.
			//rawPassword : 사용자가 입력한 패스워드
			//encodePassword : DB에 저장된 패스워드

			if(passwordEncoder.matches(password, rmember.getPassword())) {
				result = 1;	//아이디와 비밀번호가 일치하는 경우
			} else
				result = 0; //아이디는 존재하지만 비밀번호가 일치하지 않는 경우
		}
		return result;
	}

	@Override
	public int insert(Member m) {
		return dao.insert(m);
	}

	@Override
	public Member member_info(String id) {
		return dao.isId(id);
	}

	@Override
	public int isId(String id) {
		Member rmember = dao.isId(id);
		return (rmember==null) ? -1 : 1;
	}

	@Override
	public int isName(String name) {
		Member rmember = dao.isName(name);
		return (rmember==null) ? -1 : 1;
	}

	@Override
	public Social mysocial(String id) {
		return dao.mysocial(id);
	}

	@Override
	public ArrayList<Ctgry> getCtgryList(int type) {
		return dao.getCtgryList(type);
	}

	@Override
	public int myinfo_modify(String id) {
		return dao.myinfo_modify(id);
	}

	@Override
	public int social_insert(Social social) {
		return dao.social_insert(social);
	}

	@Override
	public int social_update(Social social) {
		return dao.social_update(social);
	}

	@Override
	public int isEmail(String email){
		Member rmember = dao.isEmail(email);
		return (rmember==null) ? -1 : 1;
	}

}
