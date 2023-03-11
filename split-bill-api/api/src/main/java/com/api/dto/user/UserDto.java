package com.api.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    Integer id;
    Integer groupId;
    String name;
    String photoUrl;
    Long dateCreated;
    Integer uniqueId;
}
