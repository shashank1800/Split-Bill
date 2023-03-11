package com.api.splitbill.controller;

import com.api.splitbill.dto.user_profile.*;
import com.api.splitbill.service.IUserProfileService;
import com.common.exception.KnownException;
import com.shashankbhat.repository.LoggedUsersRepository;
import com.shashankbhat.util.HelperMethods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user_profile")
public class UserProfileController {

    @Autowired
    private LoggedUsersRepository loggedUsersRepository;

    @Autowired
    private IUserProfileService userProfileService;


    @PostMapping(value = "/saveProfile")
    public ResponseEntity<?> saveProfile(@RequestBody UserProfileSaveDto userProfileSaveDto) {

        try{
            Integer uniqueId = HelperMethods.getUniqueId(loggedUsersRepository);

            userProfileService.saveProfile(uniqueId, userProfileSaveDto.getName(),
                    userProfileSaveDto.getPhotoUrl(), userProfileSaveDto.getIsNearbyVisible(),
                    userProfileSaveDto.getDistanceRange());

            return new ResponseEntity<>(uniqueId, HttpStatus.OK);
        } catch (KnownException kn) {
            return ResponseEntity.badRequest().body(kn.getErrorMessage());
        }catch (Exception ex){
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping(value = "/locationPreference")
    public ResponseEntity<?> setLocationPreference(@RequestBody SetLocationPreferenceDto locationPreferenceDto) {

        try{
            Integer uniqueId = HelperMethods.getUniqueId(loggedUsersRepository);
            userProfileService.locationPreference(uniqueId, locationPreferenceDto);

            return new ResponseEntity<>(uniqueId, HttpStatus.OK);
        } catch (Exception ex){
            return ResponseEntity.internalServerError().build();
        }
    }


    @PutMapping(value = "/updateName")
    public ResponseEntity<?> updateName(@RequestBody /*@Valid*/ UpdateNameDto updateNameDto) {

        try{
            Integer uniqueId = HelperMethods.getUniqueId(loggedUsersRepository);
            userProfileService.updateName(uniqueId, updateNameDto.getName());

            return new ResponseEntity<>(uniqueId, HttpStatus.OK);
        } catch (Exception ex){
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping(value = "/profileDetail")
    public ResponseEntity<?> getProfile() {

        try{
            Integer uniqueId = HelperMethods.getUniqueId(loggedUsersRepository);

            UserProfileDataDto profile = userProfileService.profileData(uniqueId);

            return new ResponseEntity<>(profile, HttpStatus.OK);
        } catch (Exception ex){
            return ResponseEntity.internalServerError().build();
        }
    }


    @PutMapping(value = "/updateProfilePhoto")
    public ResponseEntity<?> updateProfilePhoto(@RequestBody /*@Valid*/ UpdateProfilePhotoDto updateProfilePhotoDto) {

        try{
            Integer uniqueId = HelperMethods.getUniqueId(loggedUsersRepository);
            userProfileService.updateProfilePhoto(uniqueId, updateProfilePhotoDto.getPhotoUrl());

            return new ResponseEntity<>(uniqueId, HttpStatus.OK);
        } catch (Exception ex){
            return ResponseEntity.internalServerError().build();
        }
    }


}
