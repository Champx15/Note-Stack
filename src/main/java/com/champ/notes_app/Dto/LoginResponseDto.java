package com.champ.notes_app.Dto;

import java.time.LocalDateTime;
import java.util.Date;

public class LoginResponseDto {
    private String jwt;
    private LocalDateTime time;

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public LoginResponseDto(String jwt) {
        this.jwt = jwt;
        this.time = LocalDateTime.now();
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public LoginResponseDto() {
    }
}
