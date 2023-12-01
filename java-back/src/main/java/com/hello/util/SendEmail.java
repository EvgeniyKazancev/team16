package com.hello.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Component
public class SendEmail {

    @Value("${email.username}")
    private String mailUsername;
    @Value("${email.password}")
    private String mailPassword;
    @Value("${email.smtp.host}")
    private String mailSmtpHost;
    @Value("${email.smtp.port}")
    private String mailSmtpPort;
    @Value("${email.smtp.auth}")
    private String mailSmtpAuth;
    @Value("${email.smtp.starttls.enable}")
    private String mailSmtpStarttlsEnable;

    public boolean sendToken(String recipientEmail, String token) {

        Properties prop = new Properties();
        prop.put("mail.smtp.host", mailSmtpHost);
        prop.put("mail.smtp.port", mailSmtpPort);
        prop.put("mail.smtp.auth", mailSmtpAuth);
        prop.put("mail.smtp.starttls.enable", mailSmtpStarttlsEnable); //TLS

        Session session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(mailUsername, mailPassword);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(mailUsername));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(recipientEmail)
            );
            message.setSubject("Одноразовый код для входа.");
            message.setText("Ваш одноразовый код:\n\n" + token);
            message.setContent("<h3 align='center'>Ваш одноразовый код:</h3>" +
                    "<p align='center'>" + token + "</p>", "text/html; charset=\"UTF-8\"");

            Transport.send(message);

            System.out.println("Done");

        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}