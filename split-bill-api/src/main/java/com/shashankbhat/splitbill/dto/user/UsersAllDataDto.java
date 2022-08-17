package com.shashankbhat.splitbill.dto.user;

import com.shashankbhat.splitbill.entity.UsersEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsersAllDataDto {
    List<UserDto> data;
}
