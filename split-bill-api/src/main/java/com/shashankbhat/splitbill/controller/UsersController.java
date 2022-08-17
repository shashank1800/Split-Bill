package com.shashankbhat.splitbill.controller;

import com.shashankbhat.splitbill.dto.user.UserDto;
import com.shashankbhat.splitbill.dto.user.UsersAllDataDto;
import com.shashankbhat.splitbill.dto.user.UsersLinkDto;
import com.shashankbhat.splitbill.dto.user.UsersSaveDto;
import com.shashankbhat.splitbill.entity.UserProfileEntity;
import com.shashankbhat.splitbill.entity.UsersEntity;
import com.shashankbhat.splitbill.repository.LoggedUsersRepository;
import com.shashankbhat.splitbill.repository.UserProfileRepository;
import com.shashankbhat.splitbill.repository.UsersRepository;
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
@RequestMapping("/users")
public class UsersController {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private LoggedUsersRepository loggedUsersRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;


    @PostMapping(value = "/saveUser")
    public ResponseEntity<UsersEntity> saveUser(@RequestBody @Valid UsersSaveDto user){
        UsersEntity result = usersRepository.save(new UsersEntity(null, user.getGroupId(), user.getName(), System.currentTimeMillis(), null));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/getAllUser")
    public ResponseEntity<UsersAllDataDto> getAllUser(@RequestParam Integer groupId){
        try{

            List<UsersEntity> users = usersRepository.findByGroupId(groupId, Sort.by(Sort.Direction.ASC, "name"));

            List<UserDto> userDtoList = new ArrayList<>();

            users.forEach(usersEntity -> {
                UserProfileEntity userProfileEntity = null;
                if(usersEntity.getUniqueId() != null)
                    userProfileEntity = userProfileRepository.getById(usersEntity.getUniqueId());

                userDtoList.add(
                        new UserDto(
                                usersEntity.getId(),
                                usersEntity.getGroupId(),
                                usersEntity.getName(),
                                userProfileEntity != null ? userProfileEntity.getPhotoUrl() : null,
                                usersEntity.getDateCreated(),
                                usersEntity.getUniqueId()
                        )
                );
            });
            return new ResponseEntity<>(new UsersAllDataDto(userDtoList), HttpStatus.OK);
        } catch (Exception ex){
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping(value = "/deleteUser")
    public ResponseEntity<UsersSaveDto> deleteUser(@RequestBody @Valid UsersSaveDto user){
        usersRepository.deleteById(user.getId());
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping(value = "/linkUser")
    public ResponseEntity<String> linkUser(@RequestBody @Valid UsersLinkDto usersLinkDto){
        usersRepository.linkUser(usersLinkDto.getUniqueId(), usersLinkDto.getId());
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

}
