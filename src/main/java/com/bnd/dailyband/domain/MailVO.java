package com.bnd.dailyband.domain;

import lombok.Getter;
import lombok.Setter;

import java.security.SecureRandom;
import java.util.Random;

@Getter
@Setter
public class MailVO {

	private String from="zPAmt5sa7xvC8tvjFtVLkUoA4qU3zSyMz+1Bqc5vXaE=";
	private String receiver;
	private String subject="인증 번호를 입력해주세요";
	private String content="";

	private Random random = new SecureRandom();

	public MailVO() {
		int length = 8;
		String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		StringBuilder authCode = new StringBuilder(length);
		for (int i = 0; i < length; i++) {
			authCode.append(chars.charAt(random.nextInt(chars.length())));
		}
		content = authCode.toString();
	}

}

