package com.api.dto.user_profile;

import lombok.Data;

@Data
public class UserProfileSaveDto {
    private String name;
    private String photoUrl;
    private Boolean isNearbyVisible;
    private Double distanceRange;
}
