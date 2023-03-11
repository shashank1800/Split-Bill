package com.api.controller;

import com.common.exception.KnownException;
import com.shashankbhat.repository.LoggedUsersRepository;
import com.api.dto.groups.GroupsAllDataDto;
import com.api.dto.groups.GroupsEntityDto;
import com.api.dto.groups.GroupsSaveDto;
import com.api.service.IGroupService;
import com.shashankbhat.util.HelperMethods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/group")
class GroupsController {

    @Autowired
    private LoggedUsersRepository loggedUsersRepository;

    @Autowired
    private IGroupService groupService;

    @PostMapping(value = "/saveGroup")
    public ResponseEntity<?> saveGroup(@RequestBody /*@Valid*/ GroupsSaveDto group) {
        try {
            Integer uniqueId = HelperMethods.getUniqueId(loggedUsersRepository);

            GroupsEntityDto result = groupService.saveGroup(uniqueId, group);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (KnownException kn) {
            return ResponseEntity.badRequest().body(kn.getErrorMessage());
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping(value = "/allGroups")
    public ResponseEntity<?> getAllGroups() {

        Integer uniqueId = HelperMethods.getUniqueId(loggedUsersRepository);

        try {
            List<GroupsEntityDto> result = groupService.getAllGroups(uniqueId);
            return new ResponseEntity<>(new GroupsAllDataDto(result), HttpStatus.OK);
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().build();
        }
    }


}


