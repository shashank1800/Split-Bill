package com.shashankbhat.util;

import com.shashankbhat.entity.LoggedUsersEntity;
import com.shashankbhat.repository.LoggedUsersRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class HelperMethods {
    public static Integer getUniqueId(LoggedUsersRepository loggedUsersRepository) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        LoggedUsersEntity loggedUser =  loggedUsersRepository.findOneByUsername(userDetails.getUsername());
        return loggedUser.getId();
    }
}
