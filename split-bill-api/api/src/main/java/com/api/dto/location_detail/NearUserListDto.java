package com.api.dto.location_detail;

import com.shashankbhat.dto.location_detail.GetNearUserDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class NearUserListDto {
    private List<GetNearUserDto> users;
}
