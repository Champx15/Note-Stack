package com.champ.notes_app.RController;

import com.champ.notes_app.Entity.Note;
import com.champ.notes_app.Service.NotesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("/notes")
@CrossOrigin(origins = "https://note-stack.netlify.app")
public class RestController {
    @Autowired
    private NotesService service;

    @GetMapping
    public List<Note> getAllNotes(){
        return service.fetchAllNotes();
    }

    @PostMapping("/add")
    public Note createNote(@RequestBody Note notes){
        return service.createNote(notes.getTitle(),notes.getBody());
    }
    @DeleteMapping("/delete/{id}")
    public void deleteNote(@PathVariable Long id){
        service.removeNote(id);
    }


}
