package com.shashankbhat.splitbill.service;

import com.shashankbhat.splitbill.dto.user_profile.SetLocationPreferenceDto;

public interface IUserProfileService {

    void saveProfile(Integer uniqueId, String name, String photoUrl, Boolean isNearbyVisible, Double distanceRange);

    void locationPreference(Integer uniqueId, SetLocationPreferenceDto locationPreferenceDto);

    void updateName(Integer uniqueId, String name);

    void updateLocationRange(Integer uniqueId, Double distanceRange);
}
