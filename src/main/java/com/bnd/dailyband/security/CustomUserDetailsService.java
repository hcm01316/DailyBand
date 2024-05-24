package com.bnd.dailyband.security;

import java.util.ArrayList;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
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

			Collection<SimpleGrantedAuthority> roles = new ArrayList<SimpleGrantedAuthority>();
			
			roles.add(new SimpleGrantedAuthority(users.getMBR_TY()));
			
			UserDetails user = new User(username, users.getMBR_PWD(), roles);
			
	return user;
	}


}
