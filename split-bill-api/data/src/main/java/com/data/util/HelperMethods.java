package com.data.util;

import com.data.entity.LoggedUsersEntity;
import com.data.repository.LoggedUsersRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class HelperMethods {
    public static Integer getUniqueId(LoggedUsersRepository loggedUsersRepository) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        LoggedUsersEntity loggedUser =  loggedUsersRepository.findOneByUsername(userDetails.getUsername());
        return loggedUser.getId();
    }
}
