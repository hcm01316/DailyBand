package com.bnd.dailyband.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import com.bnd.dailyband.domain.Member;
import com.bnd.dailyband.mybatis.mapper.MemberMapper;

@Component
public class CustomUserDetailsService implements UserDetailsService {
	private static final Logger logger
	= LoggerFactory.getLogger(CustomUserDetailsService.class);

	@Autowired
	private MemberMapper dao;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		logger.info("username 로그인한 아이디 : " + username);
		Member users = dao.isId(username);
		if (users==null) {
			logger.info("username " + username + " not found");
			
			throw new UsernameNotFoundException("username " + username + " not found");
			
		}

	return users;
	}


}
