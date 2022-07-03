package com.shashankbhat.splitbill.controller;

import com.shashankbhat.splitbill.dto.user_profile.SetLocationPreferenceDto;
import com.shashankbhat.splitbill.dto.user_profile.UpdateNameDto;
import com.shashankbhat.splitbill.dto.user_profile.UserProfileDataDto;
import com.shashankbhat.splitbill.dto.user_profile.UserProfileSaveDto;
import com.shashankbhat.splitbill.entity.UserProfileEntity;
import com.shashankbhat.splitbill.repository.LoggedUsersRepository;
import com.shashankbhat.splitbill.service.IUserProfileService;
import com.shashankbhat.splitbill.util.HelperMethods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/user_profile")
public class UserProfileController {

    @Autowired
    private LoggedUsersRepository loggedUsersRepository;

    @Autowired
    private IUserProfileService userProfileService;


    @PostMapping(value = "/saveProfile")
    public ResponseEntity<?> saveProfile(@RequestBody @Valid UserProfileSaveDto userProfileSaveDto) {

        try{
            Integer uniqueId = HelperMethods.getUniqueId(loggedUsersRepository);

            userProfileService.saveProfile(uniqueId, userProfileSaveDto.getName(),
                    userProfileSaveDto.getPhotoUrl(), userProfileSaveDto.getIsNearbyVisible(), userProfileSaveDto.getDistanceRange());

            return new ResponseEntity<>(uniqueId, HttpStatus.OK);
        } catch (Exception ex){
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping(value = "/locationPreference")
    public ResponseEntity<?> setLocationPreference(@RequestBody @Valid SetLocationPreferenceDto locationPreferenceDto) {

        try{
            Integer uniqueId = HelperMethods.getUniqueId(loggedUsersRepository);
            userProfileService.locationPreference(uniqueId, locationPreferenceDto);

            return new ResponseEntity<>(uniqueId, HttpStatus.OK);
        } catch (Exception ex){
            return ResponseEntity.internalServerError().build();
        }
    }


    @PutMapping(value = "/updateName")
    public ResponseEntity<?> updateName(@RequestBody @Valid UpdateNameDto updateNameDto) {

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
    public ResponseEntity<?> updateProfilePhoto(@RequestBody String photoUrl) {

        try{
            Integer uniqueId = HelperMethods.getUniqueId(loggedUsersRepository);
            userProfileService.updateProfilePhoto(uniqueId, photoUrl);

            return new ResponseEntity<>(uniqueId, HttpStatus.OK);
        } catch (Exception ex){
            return ResponseEntity.internalServerError().build();
        }
    }


}
