package com.shashankbhat.splitbill.controller;

import com.shashankbhat.splitbill.dto.UsersSaveDto;
import com.shashankbhat.splitbill.entity.UsersEntity;
import com.shashankbhat.splitbill.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Date;

@RestController
@RequestMapping("/users")
public class UsersController {


    @Autowired
    private UsersRepository usersRepository;

    @PostMapping(value = "/saveUser")
    public ResponseEntity<Integer> saveGroup(@RequestBody @Valid UsersSaveDto user){
        UsersEntity result = usersRepository.save(new UsersEntity(null, user.getGroupId(), user.getName(), System.currentTimeMillis()));

        return new ResponseEntity<Integer>(result.getId(), HttpStatus.OK);
    }

}
