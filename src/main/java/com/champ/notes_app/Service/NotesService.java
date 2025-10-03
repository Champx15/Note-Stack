package com.champ.notes_app.Service;

import com.champ.notes_app.Entity.Note;
import com.champ.notes_app.Repo.NotesRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotesService implements IServiceNotes{
    @Autowired

    private NotesRepo repo;
    public Note createNote(String title, String body){
        Note note = new Note(title,body);
        return repo.save(note);

    }

    @Override
    public List<Note> fetchAllNotes() {
        return repo.findAll();
    }

    @Override
    public void removeNote(Long id) {
        repo.deleteById(id);
    }
}
