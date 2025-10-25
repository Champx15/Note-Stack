package com.champ.notes_app.Controller;

import com.champ.notes_app.Config.JwtService;
import com.champ.notes_app.Entity.Note;
import com.champ.notes_app.Service.NotesService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("/notes")
@CrossOrigin(origins = {"https://note-stack.netlify.app"})
public class NoteController {
    @Autowired
    private NotesService service;
    @Autowired 
    private JwtService jwtService;

    @GetMapping()
    public ResponseEntity<List<Note>> getNotes(Authentication authentication){
        Object principal = authentication.getPrincipal();
        String email=null;
        if(principal instanceof UserDetails userDetails){
        email = userDetails.getUsername();
        }
        else if(principal instanceof OAuth2User oAuth2User){
            email=oAuth2User.getAttribute("email");
        }
        List<Note> notes = service.fetchNotes(email);
        return new ResponseEntity<>(notes, HttpStatus.OK);
    }

    @PostMapping("/add")
    public Note createNote(@RequestBody Note notes, HttpServletRequest request){
        String email = jwtService.extractEmailFromRequest(request);
        return service.createNote(notes.getTitle(),notes.getBody(),email);
    }
    @DeleteMapping("/delete/{id}")
    public void deleteNote(@PathVariable Long id){
        service.removeNote(id);
    }


}
