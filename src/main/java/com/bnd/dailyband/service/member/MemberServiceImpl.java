package com.bnd.dailyband.service.member;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.bnd.dailyband.domain.Ctgry;
import com.bnd.dailyband.domain.Member;
import com.bnd.dailyband.domain.Social;
import com.bnd.dailyband.mybatis.mapper.MemberMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class MemberServiceImpl implements MemberService{

	private final MemberMapper memberMapper;
	private MemberMapper dao;
	private PasswordEncoder passwordEncoder;

	@Autowired
	public MemberServiceImpl(MemberMapper dao, PasswordEncoder passwordEncoder, MemberMapper memberMapper) {
		this.dao = dao;
		this.passwordEncoder=passwordEncoder;
		this.memberMapper = memberMapper;
	}

	@Value("${s3.bucket}")
	private String bucket;

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
	public int myinfo_modify(Member member, String id) {
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("MBR_ID", id);
		paramMap.put("MBR_NCNM", member.getMBR_NCNM());
		paramMap.put("MBR_AGE", member.getMBR_AGE());
		paramMap.put("MBR_GENDER", member.getMBR_GENDER());
		paramMap.put("MBR_PREFER_AREA", member.getMBR_PREFER_AREA());
		paramMap.put("MBR_ACT_REALM", member.getMBR_ACT_REALM());
		paramMap.put("MBR_PREFER_GENRE", member.getMBR_PREFER_GENRE());
		paramMap.put("MBR_INTRO", member.getMBR_INTRO());

		return dao.myinfo_modify(paramMap);
	}


	@Override
	public int social_insert(Social social,String id) {
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("MBR_ID", id);
		paramMap.put("INSTA_ADDRM",social.getINSTA_ADDR());
		paramMap.put("YT_ADDR", social.getYT_ADDR());
		paramMap.put("SC_ADDR", social.getSC_ADDR());
		paramMap.put("SPOTI_ADDR", social.getSPOTI_ADDR());
		return dao.social_insert(paramMap);
	}

	@Override
	public int social_update(Social social, String id) {
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("MBR_ID", id);
		paramMap.put("INSTA_ADDR",social.getINSTA_ADDR());
		paramMap.put("YT_ADDR", social.getYT_ADDR());
		paramMap.put("SC_ADDR", social.getSC_ADDR());
		paramMap.put("SPOTI_ADDR", social.getSPOTI_ADDR());
		return dao.social_update(paramMap);
	}

	@Override
	public int isEmail(String email){
		Member rmember = dao.isEmail(email);
		return (rmember==null) ? -1 : 1;
	}

	@Override
	public void imageupdate(String url, String id) {
		dao.imageupdate(url,id);
	}

	@Override
	public Member myallinfo(String id) {
		return dao.myallinfo(id);
	}

	@Override
	public String findIdByEmail(String email) {
		return dao.findIdByEmail(email);
	}

}
