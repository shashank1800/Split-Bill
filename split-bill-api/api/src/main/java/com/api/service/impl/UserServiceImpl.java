package com.api.service.impl;

import com.api.service.IUserProfileService;
import com.api.service.IUserService;
import com.shashankbhat.entity.GroupsEntity;
import com.shashankbhat.entity.UsersEntity;
import com.common.exception.KnownException;
import com.shashankbhat.repository.GroupsRepository;
import com.shashankbhat.repository.LoggedUsersRepository;
import com.shashankbhat.repository.UserProfileRepository;
import com.shashankbhat.repository.UsersRepository;
import com.api.dto.user.UserDto;
import com.api.dto.user.UsersAllDataDto;
import com.api.dto.user.UsersLinkDto;
import com.api.dto.user.UsersSaveDto;
import com.common.util.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private LoggedUsersRepository loggedUsersRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private GroupsRepository groupsRepository;

    @Autowired
    private IUserProfileService userProfileService;

    @Override
    public UsersEntity saveUser(UsersSaveDto user) throws KnownException {
        Valid<UsersEntity> userValid = UsersEntity.create(null, user.getGroupId(), user.getName(),
                System.currentTimeMillis(), null);
        if(userValid.isFailed())
            throw new KnownException(userValid.getMessage());

        return usersRepository.save(userValid.getValue());
    }

    @Override
    public UsersAllDataDto getAllUser(Integer groupId) {
        GroupsEntity groupsEntity = groupsRepository.getById(groupId);
        List<UserDto> userDtoList = userProfileService.getAllUsers(groupsEntity.getId());
        return new UsersAllDataDto(userDtoList);
    }

    @Override
    public UsersSaveDto deleteUser(UsersSaveDto user) {
        usersRepository.deleteById(user.getId());
        return user;
    }

    @Override
    public String linkUser(UsersLinkDto usersLinkDto) {
        usersRepository.linkUser(usersLinkDto.getUniqueId(), usersLinkDto.getId());
        return "Success";
    }
}
