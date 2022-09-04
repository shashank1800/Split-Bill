package com.shashankbhat.splitbill.service.impl;

import com.shashankbhat.splitbill.dto.user.UserDto;
import com.shashankbhat.splitbill.dto.user_profile.SetLocationPreferenceDto;
import com.shashankbhat.splitbill.dto.user_profile.UserProfileDataDto;
import com.shashankbhat.splitbill.entity.GroupsEntity;
import com.shashankbhat.splitbill.entity.LocationDetailEntity;
import com.shashankbhat.splitbill.entity.UserProfileEntity;
import com.shashankbhat.splitbill.entity.UsersEntity;
import com.shashankbhat.splitbill.repository.GroupsRepository;
import com.shashankbhat.splitbill.repository.LocationDetailRepository;
import com.shashankbhat.splitbill.repository.UserProfileRepository;
import com.shashankbhat.splitbill.repository.UsersRepository;
import com.shashankbhat.splitbill.service.IUserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserProfileServiceImpl implements IUserProfileService {


    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private GroupsRepository groupsRepository;

    @Autowired
    private LocationDetailRepository locationDetailRepository;

    @Override
    public void saveProfile(Integer uniqueId, String name, String photoUrl, Boolean isNearbyVisible, Double distanceRange) {
        try{
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
                            distanceRange,
                            null,
                            null,
                            System.currentTimeMillis()
                    )
            );
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
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

    @Override
    public UserProfileEntity getProfile(Integer uniqueId) {
        return userProfileRepository.findOneByUniqueId(uniqueId);
    }


    @Override
    public UserProfileDataDto profileData(Integer uniqueId) {

        UserProfileEntity userProfileEntity =  userProfileRepository.findOneByUniqueId(uniqueId);
        LocationDetailEntity locationDetailEntity = locationDetailRepository.findOneByUniqueId(uniqueId);

        if(userProfileEntity == null || locationDetailEntity == null)
            return null;

        return new UserProfileDataDto(
                uniqueId,
                userProfileEntity.getName(),
                userProfileEntity.getPhotoUrl(),
                userProfileEntity.getIsNearbyVisible(),
                locationDetailEntity.getDistanceRange()
        );
    }

    @Transactional
    @Override
    public void updateProfilePhoto(Integer uniqueId, String name) {
        userProfileRepository.updateProfilePhoto(uniqueId, name);
    }

    @Override
    public List<UserDto> getAllUsers(Integer groupId){
        GroupsEntity groupsEntity = groupsRepository.getById(groupId);
        List<UserDto> usersList = new ArrayList<>();
        List<UsersEntity> users = usersRepository.findByGroupId(groupsEntity.getId());

        users.forEach(usersEntity -> {
            String profileUrl = null;
            if(usersEntity.getUniqueId() != null){
                profileUrl = getProfile(usersEntity.getUniqueId()).getPhotoUrl();
            }
            usersList.add(new UserDto(usersEntity.getId(), usersEntity.getGroupId(), usersEntity.getName(), profileUrl, usersEntity.getDateCreated(), usersEntity.getUniqueId()));
        });

        return usersList;
    }

}
