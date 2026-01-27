package com.jichan.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @InjectMocks
    private EmailService emailService;

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private MimeMessage mimeMessage;

    @Test
    @DisplayName("이메일 인증 메일 전송 성공")
    void sendVerificationEmail_success() throws MessagingException {
        // given
        String to = "test@example.com";
        String token = "testToken";
        given(mailSender.createMimeMessage()).willReturn(mimeMessage);

        // when
        emailService.sendVerificationEmail(to, token);

        // then
        verify(mailSender).send(any(MimeMessage.class));
    }

    @Test
    @DisplayName("비밀번호 재설정 메일 전송 성공")
    void sendPasswordResetEmail_success() throws MessagingException {
        // given
        String to = "test@example.com";
        String token = "testToken";
        given(mailSender.createMimeMessage()).willReturn(mimeMessage);

        // when
        emailService.sendPasswordResetEmail(to, token);

        // then
        verify(mailSender).send(any(MimeMessage.class));
    }
}
