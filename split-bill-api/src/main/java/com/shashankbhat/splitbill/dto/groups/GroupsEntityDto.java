package com.shashankbhat.splitbill.dto.groups;

import com.shashankbhat.splitbill.dto.user.UserDto;
import com.shashankbhat.splitbill.entity.GroupsEntity;
import com.shashankbhat.splitbill.entity.UsersEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupsEntityDto {
    public GroupsEntity group;
    private List<UserDto> userList;
}