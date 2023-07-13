package com.api.dto.user_profile;

import lombok.Data;

@Data
public class SetLocationPreferenceDto {
    private Boolean isNearbyVisible;
    private Double latitude;
    private Double longitude;
}
