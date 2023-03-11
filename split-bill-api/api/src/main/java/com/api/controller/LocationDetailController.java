package com.api.controller;

import com.api.service.IUserProfileService;
import com.common.exception.KnownException;
import com.shashankbhat.repository.LoggedUsersRepository;
import com.api.dto.location_detail.LocationDetailDto;
import com.api.dto.location_detail.NearUserListDto;
import com.api.dto.user_profile.UpdateLocationRangeDto;
import com.api.service.ILocationDetailService;
import com.shashankbhat.util.HelperMethods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/location_detail")
public class LocationDetailController {

    @Autowired
    private LoggedUsersRepository loggedUsersRepository;

    @Autowired
    private IUserProfileService userProfileService;

    @Autowired
    private ILocationDetailService locationDetailService;

    @PutMapping(value = "/updateLocationRange")
    public ResponseEntity<?> updateLocationRange(@RequestBody /*@Valid*/ UpdateLocationRangeDto updateLocationRangeDto) {

        try{
            Integer uniqueId = HelperMethods.getUniqueId(loggedUsersRepository);
            userProfileService.updateLocationRange(uniqueId, updateLocationRangeDto.getDistanceRange());

            return new ResponseEntity<>(uniqueId, HttpStatus.OK);
        } catch (Exception ex){
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping(value = "/getNearUsers")
    public ResponseEntity<?> getNearUsers(@RequestBody LocationDetailDto locationDetailDto) {
        try{
            Integer uniqueId = HelperMethods.getUniqueId(loggedUsersRepository);

            NearUserListDto nearUsers = locationDetailService.getNearUsers(uniqueId,
                    locationDetailDto.getLatitude(), locationDetailDto.getLongitude());
            return new ResponseEntity<>(nearUsers, HttpStatus.OK);
        }catch (KnownException kn) {
            return ResponseEntity.badRequest().body(kn.getErrorMessage());
        } catch (Exception ex){
            return ResponseEntity.internalServerError().build();
        }
    }
}
