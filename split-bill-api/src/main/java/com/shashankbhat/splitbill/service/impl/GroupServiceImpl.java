package com.shashankbhat.splitbill.service.impl;

import com.shashankbhat.splitbill.dto.groups.GroupsEntityDto;
import com.shashankbhat.splitbill.dto.groups.GroupsSaveDto;
import com.shashankbhat.splitbill.dto.user.UserDto;
import com.shashankbhat.splitbill.entity.GroupsEntity;
import com.shashankbhat.splitbill.entity.UserProfileEntity;
import com.shashankbhat.splitbill.entity.UsersEntity;
import com.shashankbhat.splitbill.exception.KnownException;
import com.shashankbhat.splitbill.repository.GroupsRepository;
import com.shashankbhat.splitbill.repository.LoggedUsersRepository;
import com.shashankbhat.splitbill.repository.UsersRepository;
import com.shashankbhat.splitbill.service.IGroupService;
import com.shashankbhat.splitbill.service.IUserProfileService;
import com.shashankbhat.splitbill.util.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

        // TODO: Move this to transaction
        GroupsEntity groupsEntity = groupsRepository.save(groupsEntityResult.getValue());

        usersList.forEach(user -> user.setGroupId(groupsEntity.getId()));
        usersRepository.saveAll(usersList);

        return new GroupsEntityDto(groupsEntity, userProfileService.getAllUsers(groupsEntity.getId()));
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
