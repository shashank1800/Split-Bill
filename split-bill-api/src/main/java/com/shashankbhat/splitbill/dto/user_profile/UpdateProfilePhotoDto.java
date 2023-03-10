package com.shashankbhat.splitbill.dto.user_profile;

import lombok.Data;


@Data
public class UpdateProfilePhotoDto {
//    @NotEmpty(message = ErrorMessage.AVATAR_CANNOT_BE_EMPTY)
    private String photoUrl;
}