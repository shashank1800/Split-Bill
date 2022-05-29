package com.shashankbhat.splitbill.dto.user_profile;

import com.shashankbhat.splitbill.exception.ErrorMessage;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class UserProfileSaveDto {

    @NotEmpty(message = ErrorMessage.USER_NAME_CANNOT_BE_EMPTY)
    private String name;
    @NotEmpty(message = ErrorMessage.AVATAR_CANNOT_BE_EMPTY)
    private String photoUrl;
    @NotNull(message = ErrorMessage.IS_NEAR_VISIBLE_CANNOT_BE_EMPTY)
    private Boolean isNearbyVisible;
    @NotNull(message = ErrorMessage.DISTANCE_RANGE_CANNOT_BE_EMPTY)
    private Double distanceRange;
}
