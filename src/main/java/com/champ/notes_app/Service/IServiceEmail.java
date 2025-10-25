package com.champ.notes_app.Service;

import jakarta.mail.MessagingException;

import java.io.UnsupportedEncodingException;

public interface IServiceEmail {
    public void sendHtmlEmail(String to, String subject,String otp) throws MessagingException, UnsupportedEncodingException;

}
