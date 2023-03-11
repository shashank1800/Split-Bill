package com.api.service;

import com.common.exception.KnownException;
import com.api.dto.groups.GroupsEntityDto;
import com.api.dto.groups.GroupsSaveDto;

import java.util.List;

public interface IGroupService {
    GroupsEntityDto saveGroup(Integer uniqueId, GroupsSaveDto group) throws KnownException;

    List<GroupsEntityDto> getAllGroups(Integer uniqueId);
}
