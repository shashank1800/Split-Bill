package com.api.splitbill.dto.user_profile;

import lombok.Data;


@Data
public class UpdateNameDto {
//    @NotEmpty(message = ErrorMessage.USER_NAME_CANNOT_BE_EMPTY)
    private String name;
}
