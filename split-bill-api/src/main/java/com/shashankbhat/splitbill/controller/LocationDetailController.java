package com.shashankbhat.splitbill.controller;

import com.shashankbhat.splitbill.dto.location_detail.GetNearUserDto;
import com.shashankbhat.splitbill.dto.location_detail.LocationDetailDto;
import com.shashankbhat.splitbill.dto.user_profile.UpdateLocationRangeDto;
import com.shashankbhat.splitbill.dto.user_profile.UpdateNameDto;
import com.shashankbhat.splitbill.exception.KnownException;
import com.shashankbhat.splitbill.repository.LoggedUsersRepository;
import com.shashankbhat.splitbill.service.ILocationDetailService;
import com.shashankbhat.splitbill.service.IUserProfileService;
import com.shashankbhat.splitbill.util.HelperMethods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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
    public ResponseEntity<?> updateLocationRange(@RequestBody @Valid UpdateLocationRangeDto updateLocationRangeDto) {

        try{
            Integer uniqueId = HelperMethods.getUniqueId(loggedUsersRepository);
            userProfileService.updateLocationRange(uniqueId, updateLocationRangeDto.getDistanceRange());

            return new ResponseEntity<>(uniqueId, HttpStatus.OK);
        } catch (Exception ex){
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping(value = "/getNearUsers")
    public ResponseEntity<?> getNearUsers(@RequestBody @Valid LocationDetailDto locationDetailDto){
        try{
            Integer uniqueId = HelperMethods.getUniqueId(loggedUsersRepository);

            List<GetNearUserDto> nearUsers = locationDetailService.getNearUsers(uniqueId,
                    locationDetailDto.getLatitude(), locationDetailDto.getLongitude());
            return new ResponseEntity<>(nearUsers, HttpStatus.OK);
        }catch (KnownException kn){
            return ResponseEntity.badRequest().body(kn.getLocalizedMessage());
        } catch (Exception ex){
            return ResponseEntity.internalServerError().build();
        }
    }
}
