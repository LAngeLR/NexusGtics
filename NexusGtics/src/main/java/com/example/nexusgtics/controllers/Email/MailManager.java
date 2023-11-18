package com.example.nexusgtics.controllers.Email;

import com.google.api.client.util.Value;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;


@Component
public class MailManager {

    JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String sender;

    public  MailManager(JavaMailSender javaMailSender){
        this.javaMailSender = javaMailSender;
    }

    public void sendMessage(String email, String messageEmail){
        MimeMessage message = javaMailSender.createMimeMessage();

        try{
            message.setSubject("Prueba de correo");
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(email);
            helper.setText(messageEmail);
            helper.setFrom(sender);
            javaMailSender.send(message);

        } catch (MessagingException e){
            throw new RuntimeException(e);
        }

    }

}
