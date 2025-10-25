package com.champ.notes_app.Controller;

import com.champ.notes_app.Config.JwtService;
import com.champ.notes_app.Dto.*;
import com.champ.notes_app.Entity.User;
import com.champ.notes_app.Repo.LoginRepo;
import com.champ.notes_app.Service.EmailService;
import com.champ.notes_app.Service.OtpService;
import com.champ.notes_app.Service.RedisService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@RestController
@CrossOrigin(origins = {"https://note-stack.netlify.app"})
public class LoginController {
    @Autowired
    private LoginRepo repo;
    @Autowired
    private AuthenticationManager authManager;
    private String jwt=null;
    @Autowired
    private JwtService jwtService;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
    @Autowired
    private OtpService otpService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private RedisService redisService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> authenticate(@RequestBody LoginRequestDto request){
        Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(),request.getPass()));
            LoginResponseDto responseDto=null;
        if(authentication.isAuthenticated()){
            jwt = jwtService.generateToken(request.getEmail());
            responseDto = new LoginResponseDto(jwt);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }
        else {
             responseDto = new LoginResponseDto(jwt);
            return new ResponseEntity<>(responseDto,HttpStatus.UNAUTHORIZED);
        }

    }

    @PostMapping("/login/add/send-mail")
    public ResponseEntity<HttpStatus> sendEmail(@RequestBody EmailRequestDto request) throws MessagingException, UnsupportedEncodingException {
        User findUser = repo.findByEmail(request.getEmail());
        if(findUser!=null){
            return new ResponseEntity<>(HttpStatus.ALREADY_REPORTED);
        }

        String otp= otpService.generateOtp();
        redisService.saveOtp(request.getEmail(),otp);
        emailService.sendHtmlEmail(request.getEmail(),"User Authentication",otp);
         return new ResponseEntity<>(HttpStatus.OK);
    }


    @PostMapping("/login/add/verify")
    public ResponseEntity<HttpStatus> addUser(@RequestBody SigupVerifyDto request) {
        String otp = redisService.getOtp(request.getEmail());
        String receivedOtp = String.valueOf(request.getOtp());
        if (receivedOtp.equals(otp)) {
            String encodedPass = encoder.encode(request.getPass());
            User user = new User(request.getEmail(),encodedPass);
            repo.save(user);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        return  new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("/login/check-email")
    public ResponseEntity<HttpStatus> checkEmail(@RequestBody EmailRequestDto  request) throws MessagingException, UnsupportedEncodingException {
        User user = repo.findByEmail(request.getEmail());
        if(user!=null){
            String otp= otpService.generateOtp();
            redisService.saveOtp(request.getEmail(), otp);
            emailService.sendHtmlEmail(request.getEmail(), "User Authentication",otp);
            return new ResponseEntity<>(HttpStatus.OK);

        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    String email=null;
    @PostMapping("/login/verify-otp")
    public ResponseEntity<HttpStatus> forgotPass(@RequestBody ForgotPassDto request){
        email=request.getEmail();
        String otp = redisService.getOtp(request.getEmail());
        String receivedOtp = request.getOtp();
        if(receivedOtp.equals(otp)){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("/login/reset-pass")
    public  ResponseEntity<HttpStatus> resetPass(@RequestBody PassRequestDto requestDto){
        User user = repo.findByEmail(email);
        String encodedPass = encoder.encode(requestDto.getNewPass());
        user.setPassHash(encodedPass);
        repo.save(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
