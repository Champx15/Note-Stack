package com.champ.notes_app.Service;

import org.springframework.stereotype.Service;

@Service
public class OtpService {
    public String   generateOtp(){
        return String.valueOf((int)( Math.random()*Math.pow(10,6)));
    }
}
