package com.champ.notes_app.Repo;

import com.champ.notes_app.Entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotesRepo extends JpaRepository<Note,Long> {
    List<Note> findByUserEmail(String email);
}
