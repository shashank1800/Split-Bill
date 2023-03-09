package com.shashankbhat.splitbill.service.impl;

import com.shashankbhat.splitbill.dto.user.UserDto;
import com.shashankbhat.splitbill.dto.user.UsersAllDataDto;
import com.shashankbhat.splitbill.dto.user.UsersLinkDto;
import com.shashankbhat.splitbill.dto.user.UsersSaveDto;
import com.shashankbhat.splitbill.entity.GroupsEntity;
import com.shashankbhat.splitbill.entity.UsersEntity;
import com.shashankbhat.splitbill.exception.KnownException;
import com.shashankbhat.splitbill.repository.GroupsRepository;
import com.shashankbhat.splitbill.repository.LoggedUsersRepository;
import com.shashankbhat.splitbill.repository.UserProfileRepository;
import com.shashankbhat.splitbill.repository.UsersRepository;
import com.shashankbhat.splitbill.service.IUserProfileService;
import com.shashankbhat.splitbill.service.IUserService;
import com.shashankbhat.splitbill.util.Valid;
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
