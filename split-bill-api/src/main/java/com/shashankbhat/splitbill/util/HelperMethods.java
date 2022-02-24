package com.shashankbhat.splitbill.util;

import com.shashankbhat.splitbill.entity.LoggedUsersEntity;
import com.shashankbhat.splitbill.repository.LoggedUsersRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class HelperMethods {
    public static Integer getUniqueId(LoggedUsersRepository loggedUsersRepository) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        LoggedUsersEntity loggedUser =  loggedUsersRepository.findOneByUsername(userDetails.getUsername());
        return loggedUser.getId();
    }
}
