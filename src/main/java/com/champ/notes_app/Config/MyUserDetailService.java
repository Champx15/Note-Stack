package com.champ.notes_app.Config;

import com.champ.notes_app.Entity.User;
import com.champ.notes_app.Repo.LoginRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import com.champ.notes_app.Config.UserPrincipal;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailService implements UserDetailsService {
    @Autowired
    private LoginRepo repo;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = repo.findByEmail(email);
        if(user==null) throw new UsernameNotFoundException("User doesn't exist");
        return  new UserPrincipal(user);

    }
}
