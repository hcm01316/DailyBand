package com.bnd.dailyband.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bnd.dailyband.domain.Member;
import com.bnd.dailyband.mybatis.mapper.MemberMapper;

@Service
public class MemberServiceImpl implements MemberService{

	private MemberMapper dao;
	private PasswordEncoder passwordEncoder;
	
	@Override
	public int insert(Member m) {
		return dao.insert(m);
	}

	@Autowired
	public MemberServiceImpl(MemberMapper dao, PasswordEncoder passwordEncoder) {
		this.dao = dao;
		this.passwordEncoder=passwordEncoder;
	}

	
}
