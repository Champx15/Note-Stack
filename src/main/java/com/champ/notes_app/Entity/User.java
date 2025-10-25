package com.champ.notes_app.Entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import org.springframework.stereotype.Component;


import java.util.List;

@Component
@Entity
@Table(name="users")
public class User {
    @Id
    @Column(name="email")
    private String email;
    @Column(name="hash" , nullable = false)
    private String passHash;
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Note> notes;

    public String getEmail() {
        return email;
    }

    public User(String email, String passHash) {
        this.email = email;
        this.passHash = passHash;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Note> getNotes() {
        return notes;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }

    public String getPassHash() {
        return passHash;
    }

    public void setPassHash(String passHash) {
        this.passHash = passHash;
    }

    public User() {
    }
}
