package com.example.nexusgtics.controllers.Email;

import org.springframework.stereotype.Service;

@Service
public class Authenticate {

    MailManager mailManager;

    public Authenticate(MailManager mailManager){
        this.mailManager = mailManager;
    }
    public void sendMessageUser(String email, String message){
        mailManager.sendMessage(email, message);
    }
}
