package com.shashankbhat.splitbill.dto.user_profile;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UpdateLocationRangeDto {
    @NotNull
    private Double distanceRange;
}
