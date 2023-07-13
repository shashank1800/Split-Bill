package com.api.dto.user_profile;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserProfileDataDto {
    private Integer uniqueId;
    private String name;
    private String photoUrl;
    private Boolean isNearbyVisible;
    private Double distanceRange;
}
