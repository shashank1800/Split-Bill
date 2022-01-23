package com.shashankbhat.splitbill.controller;

import com.shashankbhat.splitbill.dto.user.UsersAllDataDto;
import com.shashankbhat.splitbill.dto.user.UsersSaveDto;
import com.shashankbhat.splitbill.entity.UsersEntity;
import com.shashankbhat.splitbill.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UsersController {

    @Autowired
    private UsersRepository usersRepository;

    @PostMapping(value = "/saveUser")
    public ResponseEntity<UsersEntity> saveUser(@RequestBody @Valid UsersSaveDto user){
        UsersEntity result = usersRepository.save(new UsersEntity(null, user.getGroupId(), user.getName(), System.currentTimeMillis()));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/getAllUser")
    public ResponseEntity<UsersAllDataDto> getAllUser(@RequestParam Integer groupId){
        List<UsersEntity> users = usersRepository.findByGroupId(groupId);
        return new ResponseEntity<>(new UsersAllDataDto(users), HttpStatus.OK);
    }


}
