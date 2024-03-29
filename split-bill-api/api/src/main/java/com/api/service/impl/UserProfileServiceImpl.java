package com.api.service.impl;

import com.api.service.IUserProfileService;
import com.common.exception.ErrorMessage;
import com.data.entity.GroupsEntity;
import com.data.entity.LocationDetailEntity;
import com.data.entity.UserProfileEntity;
import com.data.entity.UsersEntity;
import com.common.exception.KnownException;
import com.data.repository.GroupsRepository;
import com.data.repository.LocationDetailRepository;
import com.data.repository.UserProfileRepository;
import com.data.repository.UsersRepository;
import com.api.dto.user.UserDto;
import com.api.dto.user_profile.SetLocationPreferenceDto;
import com.api.dto.user_profile.UserProfileDataDto;
import com.common.util.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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
    public void saveProfile(Integer uniqueId, String name, String photoUrl, Boolean isNearbyVisible,
                            Double distanceRange) throws KnownException {

        Valid<UserProfileEntity> userProfileEntityValid = UserProfileEntity.create(uniqueId, name, photoUrl,
                isNearbyVisible, System.currentTimeMillis());

        if (userProfileEntityValid.isFailed())
            throw new KnownException(userProfileEntityValid.getMessage());

        Valid<LocationDetailEntity> locationDetailValid = LocationDetailEntity.create(uniqueId, distanceRange,
                null, null, System.currentTimeMillis());
        if (locationDetailValid.isFailed())
            throw new KnownException(locationDetailValid.getMessage());

        saveProfileData(userProfileEntityValid.getValue(), locationDetailValid.getValue());
    }

    @Transactional
    private void saveProfileData(UserProfileEntity userProfileEntityValid, LocationDetailEntity locationDetailValid) {
        userProfileRepository.save(userProfileEntityValid);
        locationDetailRepository.save(locationDetailValid);
    }


    @Transactional
    @Override
    public void locationPreference(Integer uniqueId, SetLocationPreferenceDto locationPreferenceDto) {

        userProfileRepository.locationPreference(locationPreferenceDto.getIsNearbyVisible(), uniqueId);
        locationDetailRepository.setLatLong(uniqueId, locationPreferenceDto.getLatitude(), locationPreferenceDto.getLongitude());
    }

    @Override
    @Transactional
    public void updateName(Integer uniqueId, String name) throws KnownException {
        if (name == null || name.isEmpty())
            throw new KnownException(ErrorMessage.USER_NAME_CANNOT_BE_EMPTY);
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
    public List<UserProfileEntity> getProfiles(List<Integer> uniqueId) {
        return userProfileRepository.findAllByUniqueIdIn(uniqueId);
    }


    @Override
    public UserProfileDataDto profileData(Integer uniqueId) {

        UserProfileEntity userProfileEntity = getProfile(uniqueId);
        LocationDetailEntity locationDetailEntity = locationDetailRepository.findOneByUniqueId(uniqueId);

        if (userProfileEntity == null || locationDetailEntity == null)
            return null;

        return new UserProfileDataDto(uniqueId, userProfileEntity.getName(), userProfileEntity.getPhotoUrl(),
                userProfileEntity.getIsNearbyVisible(), locationDetailEntity.getDistanceRange());
    }

    @Transactional
    @Override
    public void updateProfilePhoto(Integer uniqueId, String name) {
        userProfileRepository.updateProfilePhoto(uniqueId, name);
    }

    @Override
    public List<UserDto> getAllUsers(Integer groupId) {
        GroupsEntity groupsEntity = groupsRepository.getById(groupId);
        List<UserDto> usersList = new ArrayList<>();
        List<UsersEntity> users = usersRepository.findByGroupId(groupsEntity.getId());

        users.forEach(usersEntity -> {
            String profileUrl = null;
            if (usersEntity.getUniqueId() != null) {
                profileUrl = getProfile(usersEntity.getUniqueId()).getPhotoUrl();
            }
            usersList.add(new UserDto(usersEntity.getId(), usersEntity.getGroupId(), usersEntity.getName(),
                    profileUrl, usersEntity.getDateCreated(), usersEntity.getUniqueId()));
        });

        return usersList;
    }

}
