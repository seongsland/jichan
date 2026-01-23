package com.jichan.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendVerificationEmail(String to, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("지찬 이메일 인증");
        message.setText("이메일 인증을 완료하려면 다음 링크를 클릭하세요: http://localhost:3000/verify_email?token=" + token);
        mailSender.send(message);
    }

    public void sendPasswordResetEmail(String to, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("지찬 비밀번호 재설정");
        message.setText("비밀번호를 재설정하려면 다음 링크를 클릭하세요: http://localhost:3000/reset_password?token=" + token);
        mailSender.send(message);
    }
}
