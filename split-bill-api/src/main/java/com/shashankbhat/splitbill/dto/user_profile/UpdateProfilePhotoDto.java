package com.shashankbhat.splitbill.dto.user_profile;

import com.shashankbhat.splitbill.exception.ErrorMessage;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class UpdateProfilePhotoDto {
    @NotEmpty(message = ErrorMessage.AVATAR_CANNOT_BE_EMPTY)
    private String photoUrl;
}