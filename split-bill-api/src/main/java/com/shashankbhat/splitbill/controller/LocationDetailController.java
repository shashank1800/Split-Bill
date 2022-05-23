package com.shashankbhat.splitbill.controller;

import com.shashankbhat.splitbill.dto.user_profile.UpdateLocationRangeDto;
import com.shashankbhat.splitbill.dto.user_profile.UpdateNameDto;
import com.shashankbhat.splitbill.repository.LoggedUsersRepository;
import com.shashankbhat.splitbill.service.IUserProfileService;
import com.shashankbhat.splitbill.util.HelperMethods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/location_detail")
public class LocationDetailController {

    @Autowired
    private LoggedUsersRepository loggedUsersRepository;

    @Autowired
    private IUserProfileService userProfileService;

    @PutMapping(value = "/updateLocationRange")
    public ResponseEntity<?> updateLocationRange(@RequestBody @Valid UpdateLocationRangeDto updateLocationRangeDto) {

        try{
            Integer uniqueId = HelperMethods.getUniqueId(loggedUsersRepository);
            userProfileService.updateLocationRange(uniqueId, updateLocationRangeDto.getDistanceRange());

            return new ResponseEntity<>(uniqueId, HttpStatus.OK);
        } catch (Exception ex){
            return ResponseEntity.internalServerError().build();
        }
    }
}
