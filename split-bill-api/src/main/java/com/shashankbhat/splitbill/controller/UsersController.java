package com.shashankbhat.splitbill.controller;

import com.shashankbhat.splitbill.dto.user.UserDto;
import com.shashankbhat.splitbill.dto.user.UsersAllDataDto;
import com.shashankbhat.splitbill.dto.user.UsersLinkDto;
import com.shashankbhat.splitbill.dto.user.UsersSaveDto;
import com.shashankbhat.splitbill.entity.GroupsEntity;
import com.shashankbhat.splitbill.entity.UsersEntity;
import com.shashankbhat.splitbill.repository.GroupsRepository;
import com.shashankbhat.splitbill.repository.LoggedUsersRepository;
import com.shashankbhat.splitbill.repository.UserProfileRepository;
import com.shashankbhat.splitbill.repository.UsersRepository;
import com.shashankbhat.splitbill.service.IUserProfileService;
import com.shashankbhat.splitbill.util.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UsersController {

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


    @PostMapping(value = "/saveUser")
    public ResponseEntity<UsersEntity> saveUser(@RequestBody UsersSaveDto user){
        Valid<UsersEntity> userValid = UsersEntity.create(null, user.getGroupId(), user.getName(),
                System.currentTimeMillis(), null);
        UsersEntity result = usersRepository.save(userValid.getValue());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/getAllUser")
    public ResponseEntity<UsersAllDataDto> getAllUser(@RequestParam Integer groupId){
        try{
            GroupsEntity groupsEntity = groupsRepository.getById(groupId);
            List<UserDto> userDtoList = userProfileService.getAllUsers(groupsEntity.getId());
            return new ResponseEntity<>(new UsersAllDataDto(userDtoList), HttpStatus.OK);
        } catch (Exception ex){
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping(value = "/deleteUser")
    public ResponseEntity<UsersSaveDto> deleteUser(@RequestBody UsersSaveDto user){
        usersRepository.deleteById(user.getId());
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping(value = "/linkUser")
    public ResponseEntity<String> linkUser(@RequestBody UsersLinkDto usersLinkDto){
        usersRepository.linkUser(usersLinkDto.getUniqueId(), usersLinkDto.getId());
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

}
