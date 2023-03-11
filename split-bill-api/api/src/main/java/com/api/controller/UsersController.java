package com.api.controller;

import com.api.service.IUserService;
import com.shashankbhat.entity.UsersEntity;
import com.common.exception.KnownException;
import com.api.dto.user.UsersAllDataDto;
import com.api.dto.user.UsersLinkDto;
import com.api.dto.user.UsersSaveDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UsersController {

    @Autowired
    private IUserService userService;


    @PostMapping(value = "/saveUser")
    public ResponseEntity<?> saveUser(@RequestBody UsersSaveDto user) {

        try {
            UsersEntity usersEntity = userService.saveUser(user);
            return new ResponseEntity<>(usersEntity, HttpStatus.OK);
        } catch (KnownException kn) {
            return ResponseEntity.badRequest().body(kn.getErrorMessage());
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().build();
        }

    }

    @GetMapping(value = "/getAllUser")
    public ResponseEntity<?> getAllUser(@RequestParam Integer groupId) {
        try {
            UsersAllDataDto response = userService.getAllUser(groupId);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping(value = "/deleteUser")
    public ResponseEntity<UsersSaveDto> deleteUser(@RequestBody UsersSaveDto user) {
        try {
            userService.deleteUser(user);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping(value = "/linkUser")
    public ResponseEntity<String> linkUser(@RequestBody UsersLinkDto usersLinkDto) {
        try {
            String response = userService.linkUser(usersLinkDto);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().build();
        }

    }

}
