package com.shashankbhat.splitbill.dto.location_detail;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class NearUserListDto {
    private List<GetNearUserDto> users;
}
