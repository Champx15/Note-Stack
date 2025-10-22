package com.champ.notes_app.RController;

import com.champ.notes_app.Config.JwtService;
import com.champ.notes_app.Dto.LoginRequestDto;
import com.champ.notes_app.Dto.LoginResponseDto;
import com.champ.notes_app.Entity.User;
import com.champ.notes_app.Repo.LoginRepo;
import com.champ.notes_app.Repo.NotesRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

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

    @PostMapping("/login/add")
    public ResponseEntity<HttpStatus>  addUser(@RequestBody LoginRequestDto request){
        String encodedPass = encoder.encode(request.getPass());
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassHash(encodedPass);
        repo.save(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
