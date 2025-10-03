package com.champ.notes_app.Service;

import com.champ.notes_app.Entity.Note;

import java.util.List;

public interface IServiceNotes {
    public Note createNote(String title, String body);
    public List<Note> fetchAllNotes();
    public void removeNote(Long id);

}
