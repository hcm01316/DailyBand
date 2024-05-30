package com.bnd.dailyband.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MailVO {
	private String from="dailyband1@naver.com";
	private String to;
	private String subject="보안 번호를 입력해주세요";
	private String content="";

}
