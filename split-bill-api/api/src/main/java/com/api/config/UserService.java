package com.api.config;

import com.shashankbhat.entity.LoggedUsersEntity;
import com.shashankbhat.repository.LoggedUsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private LoggedUsersRepository loggedUsersRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

        LoggedUsersEntity loggedUser =  loggedUsersRepository.findOneByUsername(userName);
        return new User(loggedUser.getUsername(),loggedUser.getPassword(), new ArrayList<>());
    }

}