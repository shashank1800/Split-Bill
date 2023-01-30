package com.shashankbhat.splitbill.dto.user_profile;

import com.shashankbhat.splitbill.exception.ErrorMessage;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class UpdateNameDto {
    @NotEmpty(message = ErrorMessage.USER_NAME_CANNOT_BE_EMPTY)
    private String name;
}
