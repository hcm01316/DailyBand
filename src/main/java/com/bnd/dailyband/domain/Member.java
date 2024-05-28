package com.bnd.dailyband.domain;


import java.util.ArrayList;
import java.util.Collection;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Setter
@Getter
public class Member implements UserDetails {
	private String MBR_ID;
	private String MBR_EML_ADDR;
	private String MBR_NCNM;
	private String MBR_PWD;
	private int MBR_AGE;
	private String MBR_GENDER;
	private String MBR_PREFER_AREA;
	private String MBR_ACT_REALM;
	private String MBR_PREFER_GENRE;
	private String MBR_INTRO;
	private String MBR_PROFL_PHOTO;
	private int MBR_STTUS;
	private String MBR_TY;
	private String REG_DT;
	private int JOIN_TY;


  @Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<SimpleGrantedAuthority> roles = new ArrayList<SimpleGrantedAuthority>();

		roles.add(new SimpleGrantedAuthority(this.getMBR_TY()));
		return roles;
	}

	@Override
	public String getPassword() {
		return MBR_PWD;
	}

	//계정의 아이디를 리턴합니다.
	@Override
	public String getUsername() {
		return MBR_ID;
	}

	//계정이 만료되지 않았는지를 리턴합니다. (true:만료되지 않음)
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	//계정이 잠겨있지 않은지 리턴합니다. (true:잠겨있지 않음)
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	//계정의 패스워드가 만료되지 않았는지 리턴합니다.(true:만료되지 않음)
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	//계정이 사용가능한지를 리턴합니다.(true:사용가능)
	@Override
	public boolean isEnabled() {
		return true;
	}

	public String getAREA_IDK() {
		String area = MBR_PREFER_AREA;
		if (area.equals("A01"))
			area = "서울";
		else if (area.equals("A02"))
			area = "경기";
		else if (area.equals("A03"))
			area = "부산";
		else if (area.equals("A04"))
			area = "대구";
		else if (area.equals("A05"))
			area = "광주";
		else if (area.equals("A06"))
			area = "대전";
		else if (area.equals("A07"))
			area = "충남";
		else if (area.equals("A08"))
			area = "충북";
		else if (area.equals("A09"))
			area = "세종";
		else if (area.equals("A10"))
			area = "울산";
		else if (area.equals("A11"))
			area = "인천";
		else if (area.equals("A12"))
			area = "강원";
		else if (area.equals("A13"))
			area = "전남";
		else if (area.equals("A14"))
			area = "전북";
		else if (area.equals("A15"))
			area = "경북";
		else if (area.equals("A16"))
			area = "광남";
		else if (area.equals("A17"))
			area = "제주";
		return area;
	}

	public String getREALM_IDK() {
		String gresult[] = MBR_ACT_REALM.split(",");
		String realm = "";
		for (String a : gresult) {
			if (a.equals("R01"))
				realm += "기타/베이스 ";
			else if (a.equals("R02"))
				realm += "드럼 ";
			else if (a.equals("R03"))
				realm += "키보드 ";
			else if (a.equals("R04"))
				realm += "보컬 ";
			else if (a.equals("R05"))
				realm += "그 외 ";
		}
		return realm;
	}

	public String getGENRE_IDK() {
		String giresult[] = MBR_PREFER_GENRE.split(",");
		String genre = "";
		for (String a : giresult) {
			if (a.equals("G01"))
				genre += "팝 ";
			else if (a.equals("G02"))
				genre += "발라드 ";
			else if (a.equals("G03"))
				genre += "인디음악 ";
			else if (a.equals("G04"))
				genre += "랩·힙합 ";
			else if (a.equals("G05"))
				genre += "K-POP ";
			else if (a.equals("G06"))
				genre += "트로트 ";
			else if (a.equals("G07"))
				genre += "일렉트로닉 ";
			else if (a.equals("G08"))
				genre += "락 ";
			else if (a.equals("G09"))
				genre += "메탈 ";
			else if (a.equals("G10"))
				genre += "R&B ";
			else if (a.equals("G11"))
				genre += "재즈 ";
			else if (a.equals("G12"))
				genre += "클래식 ";
			else if (a.equals("G13"))
				genre += "뮤지컬 ";
			else if (a.equals("G14"))
				genre += "국악 ";
			else if (a.equals("G15"))
				genre += "J-POP ";
			else if (a.equals("G16"))
				genre += "월드뮤직 ";
		}
		return genre;
	}
}
