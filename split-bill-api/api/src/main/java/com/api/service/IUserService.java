package com.api.service;

import com.shashankbhat.entity.UsersEntity;
import com.common.exception.KnownException;
import com.api.dto.user.UsersAllDataDto;
import com.api.dto.user.UsersLinkDto;
import com.api.dto.user.UsersSaveDto;

public interface IUserService {
    UsersEntity saveUser(UsersSaveDto user) throws KnownException;

    UsersAllDataDto getAllUser(Integer groupId);

    UsersSaveDto deleteUser(UsersSaveDto user);

    String linkUser(UsersLinkDto usersLinkDto);
}
