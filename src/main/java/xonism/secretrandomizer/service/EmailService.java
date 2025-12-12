package xonism.secretrandomizer.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import xonism.secretrandomizer.model.Email;

import java.io.UnsupportedEncodingException;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    @Value(value = "${spring.mail.username}")
    private String from;

    private final JavaMailSender mailSender;

    public void send(Email email) {
        MimeMessage mimeMessage = getMimeMessage(email);
        send(mimeMessage);
    }

    private MimeMessage getMimeMessage(Email email) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
            mimeMessageHelper.setValidateAddresses(true);
            // outlook doesn't support emoji-only display name
            mimeMessageHelper.setFrom(new InternetAddress(from, "🕵️ REDACTED"));
            mimeMessageHelper.setTo(email.to());
            mimeMessageHelper.setSubject(email.subject());
            mimeMessageHelper.setText(email.message(), true);
        } catch (MessagingException | UnsupportedEncodingException e) {
            log.error(e.getMessage(), e);
            throw new IllegalStateException("Failed to construct email message");
        }

        return mimeMessage;
    }

    private void send(MimeMessage message) {
        try {
            mailSender.send(message);
        } catch (MailException e) {
            log.error(e.getMessage(), e);
            throw new IllegalStateException("Failed to send email");
        }
    }
}
