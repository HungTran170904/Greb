package com.greb.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.ws.rs.InternalServerErrorException;

@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSenderImpl mailSender;

    public void sendMail(String to, String subject, String content) {
        try{
            var message = mailSender.createMimeMessage();
            System.out.println("Message "+message);
            var helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom("Greb");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);
            mailSender.send(message);
        }
        catch(Exception e){
            e.printStackTrace();
            throw new InternalServerErrorException("Sending mail errors");
        }
    }
}
