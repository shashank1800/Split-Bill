package com.shashankbhat.splitbill.service;

import com.shashankbhat.splitbill.dto.user.UserDto;
import com.shashankbhat.splitbill.dto.user_profile.SetLocationPreferenceDto;
import com.shashankbhat.splitbill.dto.user_profile.UserProfileDataDto;
import com.shashankbhat.splitbill.entity.GroupsEntity;
import com.shashankbhat.splitbill.entity.UserProfileEntity;

import java.util.List;

public interface IUserProfileService {

    void saveProfile(Integer uniqueId, String name, String photoUrl, Boolean isNearbyVisible, Double distanceRange);

    void locationPreference(Integer uniqueId, SetLocationPreferenceDto locationPreferenceDto);

    void updateName(Integer uniqueId, String name);

    void updateLocationRange(Integer uniqueId, Double distanceRange);

    UserProfileEntity getProfile(Integer uniqueId);

    UserProfileDataDto profileData(Integer uniqueId);

    void updateProfilePhoto(Integer uniqueId, String name);

    List<UserDto> getAllUsers(Integer groupId);
}
