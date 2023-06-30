package com.api.service;

import com.common.exception.KnownException;
import com.api.dto.groups.GroupsEntityDto;
import com.api.dto.groups.GroupsSaveDto;
import com.data.entity.GroupsEntity;
import com.data.entity.UsersEntity;

import java.util.List;

public interface IGroupService {
    GroupsEntityDto saveGroup(Integer uniqueId, GroupsSaveDto group) throws KnownException;

    GroupsEntity addGroupAndUsers(GroupsEntity groupsEntity, List<UsersEntity> usersList);

    List<GroupsEntityDto> getAllGroups(Integer uniqueId);
}
