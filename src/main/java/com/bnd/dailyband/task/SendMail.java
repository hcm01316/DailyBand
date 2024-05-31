package com.bnd.dailyband.task;

import com.bnd.dailyband.domain.MailVO;
import jakarta.mail.internet.MimeMessage;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;

@Component
public class SendMail {
	
	private JavaMailSenderImpl mailSender;

	@Value("${jasypt.encryptor.password}")
	private String PASSWORD;
	@Autowired
	public SendMail(JavaMailSenderImpl mailSender) {
		this.mailSender=mailSender;
	}
	
	private static final Logger logger = LoggerFactory.getLogger(SendMail.class);
	
	public void sendMail(MailVO vo) {

		String changefrom = jasyptDecryt(vo.getFrom());
		vo.setFrom(changefrom);
		MimeMessagePreparator mp = new MimeMessagePreparator() {
			
			@Override
			public void prepare(MimeMessage mimeMessage) throws Exception {
				//두 번재 인자 true는 멀티 파트 메시지를 사용하겠다는 의미입니다.
				MimeMessageHelper helper
					= new MimeMessageHelper(mimeMessage, true, "UTF-8");
				helper.setFrom(vo.getFrom());
				helper.setTo(vo.getReceiver());
				helper.setSubject(vo.getSubject());
				
				//1. 문자로만 전송하는 경우
				//두 번째 인자는 html을 사용하겠다는 뜻입니다.
				helper.setText(vo.getContent(), true);

			}
		};
		mailSender.send(mp); //메일 전송합니다.
		logger.info("메일 전송했습니다.");
	}
	private String jasyptDecryt(String input){
		StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
		encryptor.setAlgorithm("PBEWithMD5AndDES");
		encryptor.setPassword(PASSWORD);
		return encryptor.decrypt(input);
	}

}
