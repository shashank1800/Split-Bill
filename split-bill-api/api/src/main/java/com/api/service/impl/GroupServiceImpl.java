package com.api.service.impl;

import com.api.service.IUserProfileService;
import com.shashankbhat.entity.GroupsEntity;
import com.shashankbhat.entity.UserProfileEntity;
import com.shashankbhat.entity.UsersEntity;
import com.common.exception.KnownException;
import com.shashankbhat.repository.GroupsRepository;
import com.shashankbhat.repository.LoggedUsersRepository;
import com.shashankbhat.repository.UsersRepository;
import com.api.dto.groups.GroupsEntityDto;
import com.api.dto.groups.GroupsSaveDto;
import com.api.dto.user.UserDto;
import com.api.service.IGroupService;
import com.common.util.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;


@Service
public class GroupServiceImpl implements IGroupService {

    @Autowired
    private GroupsRepository groupsRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private LoggedUsersRepository loggedUsersRepository;

    @Autowired
    private IUserProfileService userProfileService;

    @Override
    public GroupsEntityDto saveGroup(Integer uniqueId, GroupsSaveDto group) throws KnownException {

        Valid<GroupsEntity> groupsEntityResult = GroupsEntity.create(null, group.name, System.currentTimeMillis(),
                uniqueId);

        if (groupsEntityResult.isFailed())
            throw new KnownException(groupsEntityResult.getMessage());

        List<UsersEntity> usersList = new ArrayList<>();

        if (group.peoples != null && !group.peoples.isEmpty()) {
            for (Integer uId : group.peoples) {
                UserProfileEntity user = userProfileService.getProfile(uId);

                Valid<UsersEntity> userEntityResult = UsersEntity.create(null, null, user.getName(),
                        System.currentTimeMillis(), user.getUniqueId());
                if (userEntityResult.isFailed())
                    throw new KnownException(userEntityResult.getMessage());

                usersList.add(userEntityResult.getValue());
            }

            UserProfileEntity user = userProfileService.getProfile(uniqueId);

            Valid<UsersEntity> groupCreator = UsersEntity.create(null, null, user.getName(),
                    System.currentTimeMillis(), uniqueId);

            if (groupCreator.isFailed())
                throw new KnownException(groupCreator.getMessage());

            usersList.add(groupCreator.getValue());
        }

        GroupsEntity groupsEntity = addGroupAndUsers(groupsEntityResult.getValue(), usersList);
        return new GroupsEntityDto(groupsEntity, userProfileService.getAllUsers(groupsEntity.getId()));
    }

    @Transactional
    private GroupsEntity addGroupAndUsers(GroupsEntity groupsEntity, List<UsersEntity> usersList) {
        GroupsEntity groupsEntityResponse = groupsRepository.save(groupsEntity);

        usersList.forEach(user -> user.setGroupId(groupsEntity.getId()));
        usersRepository.saveAll(usersList);

        return groupsEntityResponse;
    }

    @Override
    public List<GroupsEntityDto> getAllGroups(Integer uniqueId) {
        List<GroupsEntity> result = groupsRepository.findAllGroupsWithUniqueId(uniqueId);
        List<GroupsEntityDto> response = new ArrayList<>();
        result.forEach(groupsEntity -> {
            List<UserDto> usersList = userProfileService.getAllUsers(groupsEntity.getId());
            response.add(new GroupsEntityDto(groupsEntity, usersList));
        });

        return response;
    }
}
