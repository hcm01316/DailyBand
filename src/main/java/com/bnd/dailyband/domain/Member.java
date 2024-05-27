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

	@Override
	public String getUsername() {
		return MBR_ID;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
