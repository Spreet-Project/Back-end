package com.team1.spreet.global.infra.email.service;

import com.team1.spreet.global.error.exception.RestApiException;
import com.team1.spreet.global.error.model.ErrorStatusCode;
import com.team1.spreet.global.infra.email.dto.EmailDto;
import com.team1.spreet.global.infra.email.model.EmailConfirm;
import com.team1.spreet.global.infra.email.repository.EmailConfirmRepository;
import java.util.Random;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender emailSender;
    private final EmailConfirmRepository emailConfirmRepository;

    public String ePw;

    private MimeMessage createMessage(String to)throws Exception {
        MimeMessage message = emailSender.createMimeMessage();
        log.info("보내는 대상 : " + to);
        log.info("인증 번호 : " + ePw);

        message.addRecipients(MimeMessage.RecipientType.TO, to);//보내는 대상
        message.setSubject("Spreet 이메일 인증");

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

    public String createKey() {
        StringBuilder key = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 6; i++) {    // 인증코드 6자리
            key.append((random.nextInt(10)));
        }
        return key.toString();
    }

    private void upsert(EmailConfirm emailConfirm) {
        EmailConfirm emailConfirm1 = emailConfirmRepository.findByEmail(emailConfirm.getEmail()).orElse(null);
        if(emailConfirmRepository.findByEmail(emailConfirm.getEmail()).isPresent()){
            //update
            emailConfirm1.setEmail(emailConfirm.getEmail());
            emailConfirmRepository.save(emailConfirm1);
        }
        else
            emailConfirmRepository.save(emailConfirm);
    }

    public void sendSimpleMessage(String email)throws Exception {
        ePw = createKey();
        MimeMessage message = createMessage(email);
        upsert(new EmailConfirm(email,ePw));

        try {   //예외 처리
            emailSender.send(message);
        } catch (MailException e) {
            e.printStackTrace();
            throw new RestApiException(ErrorStatusCode.DELETED_ACCOUNT);
        }
    }

    public void emailConfirm(EmailDto emailDto) {
        EmailConfirm emailConfirm = emailConfirmRepository.findByEmail(emailDto.getEmail()).orElseThrow(
                () -> new RestApiException(ErrorStatusCode.EMAIL_CONFIRM_NULL_EXCEPTION)
        );
        if (!emailConfirm.getConfirmNumber().equals(emailDto.getConfirmCode())) {
            throw new RestApiException(ErrorStatusCode.EMAIL_CONFIRM_INCORRECT);
        }
    }
}
