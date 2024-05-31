package com.bnd.dailyband;
import org.assertj.core.api.Assertions;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.junit.jupiter.api.Test;

class JasyptConfigTest {

   // @Test
    void jasypt(){
        String url = "jdbc:mysql://database-1.cji6umw8ioeh.ap-northeast-2.rds.amazonaws.com:3306/dailyband?characterEncoding=utf8";
        String username = "dailyband1@naver.com";
        String password = "band1234@";
        String ac = "AKIAQ3EGTXGIG3ZBXEWT";
        String se = "CXj27Gd2DU/1MQhE504C3gnbk6RPIssO5I2yOxdR";
        String bu = "myfirsttest1bucket";

        String encryptUrl = jasyptEncrypt(url);
        String encryptUsername = jasyptEncrypt(username);
        String encryptPassword = jasyptEncrypt(password);
        String encryptac = jasyptEncrypt(ac);
        String encryptse = jasyptEncrypt(se);
        String encryptbu = jasyptEncrypt(bu);


        System.out.println("encryptUrl : " + encryptUrl);
        System.out.println("encryptUsername : " + encryptUsername);
        System.out.println("encryptPassword :" + encryptPassword);
        System.out.println("encryptac :" + encryptac);
        System.out.println("encryptse :" + encryptse);
        System.out.println("encryptbu :" + encryptbu);

        Assertions.assertThat(url).isEqualTo(jasyptDecryt(encryptUrl));
    }
    @Test
    void jasypt2(){
        String url = "zPAmt5sa7xvC8tvjFtVLkUoA4qU3zSyMz+1Bqc5vXaE=";

        String encryptUrl = jasyptDecryt(url);

        System.out.println("encryptUrl : " + encryptUrl);

    }

    private String jasyptEncrypt(String input) {
        String key = "hta1226";
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setAlgorithm("PBEWithMD5AndDES");
        encryptor.setPassword(key);
        return encryptor.encrypt(input);
    }

    private String jasyptDecryt(String input){
        String key = "hta1226";
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setAlgorithm("PBEWithMD5AndDES");
        encryptor.setPassword(key);
        return encryptor.decrypt(input);
    }

}