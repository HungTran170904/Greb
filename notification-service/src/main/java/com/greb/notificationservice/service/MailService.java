package com.greb.notificationservice.service;

import com.greb.notificationservice.dto.MailDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailService {
    private final JavaMailSenderImpl mailSender;

    public void sendMailToMultipleDests(MailDto dto){
        for(var mail: dto.getDestEmails()){
            sendMail(mail, dto.getSubject(), dto.getContent());
        }
    }

    private void sendMail(String to, String subject, String content) {
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
            log.error("Unable to send email to {}",to);
        }
    }
}
