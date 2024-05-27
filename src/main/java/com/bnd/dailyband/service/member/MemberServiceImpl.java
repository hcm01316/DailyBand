package com.bnd.dailyband.service.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bnd.dailyband.domain.Member;
import com.bnd.dailyband.mybatis.mapper.MemberMapper;

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

			if(passwordEncoder.matches(password, rmember.getMBR_PWD())) {
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
}
