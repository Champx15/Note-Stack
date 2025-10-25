package com.champ.notes_app.Service;


import java.io.UnsupportedEncodingException;

public interface IServiceEmail {
    public void sendHtmlEmail(String to,String otp) throws  Exception;
}
