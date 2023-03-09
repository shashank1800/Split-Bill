package com.shashankbhat.splitbill.service;

import com.shashankbhat.splitbill.dto.user.UsersAllDataDto;
import com.shashankbhat.splitbill.dto.user.UsersLinkDto;
import com.shashankbhat.splitbill.dto.user.UsersSaveDto;
import com.shashankbhat.splitbill.entity.UsersEntity;
import com.shashankbhat.splitbill.exception.KnownException;

public interface IUserService {
    UsersEntity saveUser(UsersSaveDto user) throws KnownException;

    UsersAllDataDto getAllUser(Integer groupId);

    UsersSaveDto deleteUser(UsersSaveDto user);

    String linkUser(UsersLinkDto usersLinkDto);
}
