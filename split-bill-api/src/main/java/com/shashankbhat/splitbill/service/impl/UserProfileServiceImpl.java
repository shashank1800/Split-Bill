package com.shashankbhat.splitbill.service.impl;

import com.shashankbhat.splitbill.dto.user_profile.SetLocationPreferenceDto;
import com.shashankbhat.splitbill.entity.LocationDetailEntity;
import com.shashankbhat.splitbill.entity.UserProfileEntity;
import com.shashankbhat.splitbill.repository.LocationDetailRepository;
import com.shashankbhat.splitbill.repository.UserProfileRepository;
import com.shashankbhat.splitbill.service.IUserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserProfileServiceImpl implements IUserProfileService {


    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private LocationDetailRepository locationDetailRepository;

    @Override
    public void saveProfile(Integer uniqueId, String name, String photoUrl, Boolean isNearbyVisible) {
        userProfileRepository.save(
                new UserProfileEntity(
                        uniqueId,
                        name,
                        photoUrl,
                        isNearbyVisible,
                        System.currentTimeMillis()
                )
        );

        locationDetailRepository.save(
                new LocationDetailEntity(
                        uniqueId,
                        1000d,
                        null,
                        null,
                        System.currentTimeMillis()
                )
        );
    }

    @Transactional
    @Override
    public void locationPreference(Integer uniqueId, SetLocationPreferenceDto locationPreferenceDto) {
        userProfileRepository.locationPreference(locationPreferenceDto.getIsNearbyVisible(), uniqueId);
        locationDetailRepository.setLatLong(uniqueId, locationPreferenceDto.getLatitude(), locationPreferenceDto.getLongitude());
    }

    @Transactional
    @Override
    public void updateName(Integer uniqueId, String name) {
        userProfileRepository.updateName(uniqueId, name);
    }

    @Transactional
    @Override
    public void updateLocationRange(Integer uniqueId, Double distanceRange) {
        locationDetailRepository.updateLocationRange(uniqueId, distanceRange);
    }

}
