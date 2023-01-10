package com.team1.spreet.service.implement;

import com.team1.spreet.exception.ErrorStatusCode;
import com.team1.spreet.exception.RestApiException;
import com.team1.spreet.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Random;

@Service
@Slf4j
public class EmailServiceImpl implements EmailService {

    @Autowired
    JavaMailSender emailSender;

    public static final String ePw = createKey();

    private MimeMessage createMessage(String to)throws Exception {
      log.info("보내는 대상 : " + to);
      log.info("인증 번호 : " + ePw);
        MimeMessage message = emailSender.createMimeMessage();

        message.addRecipients(MimeMessage.RecipientType.TO, to);//보내는 대상
        message.setSubject("이메일 인증 테스트");

        String msg = "";
        msg+= "<div style='margin:20px;'>";
        msg+= "<h1> SPREET </h1>";
        msg+= "<br>";
        msg+= "<p>아래 코드를 복사해 입력해주세요<p>";
        msg+= "<br>";
        msg+= "<p>감사합니다.<p>";
        msg+= "<br>";
        msg+= "<div align='center' style='border:1px solid black; font-family:verdana';>";
        msg+= "<h3 style='color:blue;'>회원가입 인증 코드입니다.</h3>";
        msg+= "<div style='font-size:130%'>";
        msg+= "CODE : <strong>";
        msg+= ePw+"</strong><div><br/> ";
        msg+= "</div>";
        message.setText(msg, "utf-8", "html");
        message.setFrom(new InternetAddress("rbals040329@gmail.com", "SPREET"));

        return message;
    }

    public static String createKey() {
        StringBuffer key = new StringBuffer();
        Random random = new Random();

        for (int i = 0; i < 8; i++) {
            int index = random.nextInt(3);

            switch (index) {
                case 0:
                    key.append((char) ((int) (random.nextInt(26)) + 97));
                    //  a~z  (ex. 1+97=98 => (char)98 = 'b')
                    break;
                case 1:
                    key.append((char) ((int) (random.nextInt(26)) + 65));
                    //  A~Z
                    break;
                case 2:
                    key.append((random.nextInt(10)));
                    // 0~9
                    break;
            }
        }
        return key.toString();
    }

    @Override
    public String sendSimpleMessage(String to)throws Exception {
        MimeMessage message = createMessage(to);
        try {   //예외 처리
            emailSender.send(message);
        } catch (MailException e) {
            e.printStackTrace();
            throw new RestApiException(ErrorStatusCode.DELETED_ACCOUNT_EXCEPTION);
        }
        return ePw;
    }


}
