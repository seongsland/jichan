package com.jichan.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${jichan.server.address}")
    private String serverAddress;

    public void sendVerificationEmail(String to, String token) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");
        helper.setTo(to);
        helper.setSubject("지찬 이메일 인증");
        String htmlContent = "<h1>이메일 인증</h1>" +
                "<p>이메일 인증을 완료하려면 아래 버튼을 클릭하세요:</p>" +
                "<a href=\"" + serverAddress + "/verify_email?token=" + token + "\" style=\"display: inline-block; padding: 10px 20px; color: white; background-color: #007bff; text-decoration: none;\">이메일 인증</a>";
        helper.setText(htmlContent, true);
        mailSender.send(message);
    }

    public void sendPasswordResetEmail(String to, String token) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");
        helper.setTo(to);
        helper.setSubject("지찬 비밀번호 재설정");
        String htmlContent = "<h1>비밀번호 재설정</h1>" +
                "<p>비밀번호를 재설정하려면 아래 버튼을 클릭하세요:</p>" +
                "<a href=\"" + serverAddress + "/reset_password?token=" + token + "\" style=\"display: inline-block; padding: 10px 20px; color: white; background-color: #007bff; text-decoration: none;\">비밀번호 재설정</a>";
        helper.setText(htmlContent, true);
        mailSender.send(message);
    }
}
