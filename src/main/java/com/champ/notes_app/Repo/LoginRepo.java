package com.champ.notes_app.Repo;

import com.champ.notes_app.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginRepo extends JpaRepository<User,String> {
    User findByEmail(String email);
}
