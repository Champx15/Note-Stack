package com.champ.notes_app.Service;

import com.champ.notes_app.Entity.Note;
import com.champ.notes_app.Entity.User;
import com.champ.notes_app.Repo.LoginRepo;
import com.champ.notes_app.Repo.NotesRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotesService implements IServiceNotes{
    @Autowired
    private NotesRepo repo;
    @Autowired
    private LoginRepo lRepo;

    public Note createNote(String title, String body, String email){
        User user = lRepo.findByEmail(email);
        Note note = new Note(title,body,user);
        return repo.save(note);

    }

    @Override
    public List<Note> fetchNotes(String email) {
        return repo.findByUserEmail(email);
    }

    @Override
    public void removeNote(Long id) {
        repo.deleteById(id);
    }

}
