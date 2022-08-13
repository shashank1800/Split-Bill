package com.shashankbhat.splitbill.controller;

import com.shashankbhat.splitbill.dto.groups.GroupsAllDataDto;
import com.shashankbhat.splitbill.dto.groups.GroupsEntityDto;
import com.shashankbhat.splitbill.dto.groups.GroupsSaveDto;
import com.shashankbhat.splitbill.dto.user.UserDto;
import com.shashankbhat.splitbill.entity.GroupsEntity;
import com.shashankbhat.splitbill.entity.UserProfileEntity;
import com.shashankbhat.splitbill.entity.UsersEntity;
import com.shashankbhat.splitbill.repository.GroupsRepository;
import com.shashankbhat.splitbill.repository.LoggedUsersRepository;
import com.shashankbhat.splitbill.repository.UsersRepository;
import com.shashankbhat.splitbill.service.IUserProfileService;
import com.shashankbhat.splitbill.util.HelperMethods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/group")
class GroupsController {
    @Autowired
    private GroupsRepository groupsRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private LoggedUsersRepository loggedUsersRepository;

    @Autowired
    private IUserProfileService userProfileService;

    @PostMapping(value = "/saveGroup")
    public ResponseEntity<GroupsEntityDto> saveGroup(@RequestBody @Valid GroupsSaveDto group) {

        Integer uniqueId = HelperMethods.getUniqueId(loggedUsersRepository);

        GroupsEntity groupsEntity = groupsRepository.save(new GroupsEntity(null, group.name, System.currentTimeMillis(), uniqueId));

        if(group.peoples != null && !group.peoples.isEmpty()){
            group.peoples.forEach(uId ->{
                UserProfileEntity user = userProfileService.getProfile(uId);
                usersRepository.save(new UsersEntity(null, groupsEntity.getId(), user.getName(), System.currentTimeMillis(), user.getUniqueId()));
            });

            UserProfileEntity user = userProfileService.getProfile(uniqueId);
            usersRepository.save(new UsersEntity(null, groupsEntity.getId(), user.getName(), System.currentTimeMillis(), uniqueId));
        }
        List<UserDto> usersList = getAllUsers(groupsEntity);

        return new ResponseEntity<>(new GroupsEntityDto(groupsEntity, usersList), HttpStatus.OK);
    }

    @GetMapping(value = "/allGroups")
    public ResponseEntity<?> getAllGroups() {

        Integer uniqueId = HelperMethods.getUniqueId(loggedUsersRepository);

        List<GroupsEntity> result = groupsRepository.findAllGroupsWithUniqueId(uniqueId);
        List<GroupsEntityDto> response = new ArrayList<>();
        result.forEach(groupsEntity -> {
            List<UserDto> usersList = getAllUsers(groupsEntity);
            response.add(new GroupsEntityDto(groupsEntity, usersList));
        });

        return new ResponseEntity<>(new GroupsAllDataDto(response), HttpStatus.OK);
    }

    public List<UserDto> getAllUsers(GroupsEntity groupsEntity){
        List<UserDto> usersList = new ArrayList<>();
        List<UsersEntity> users = usersRepository.findByGroupId(groupsEntity.getId(), Sort.by(Sort.Direction.ASC, "name"));

        users.forEach(usersEntity -> {
            String profileUrl = null;
            if(usersEntity.getUniqueId() != null){
                profileUrl = userProfileService.getProfile(usersEntity.getUniqueId()).getPhotoUrl();
            }

            usersList.add(new UserDto(usersEntity.getId(), usersEntity.getGroupId(), usersEntity.getName(), profileUrl, usersEntity.getDateCreated(), usersEntity.getUniqueId()));
        });

        return usersList;
    }
}


