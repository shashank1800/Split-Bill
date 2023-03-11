package com.api.splitbill.service;

import com.common.exception.KnownException;
import com.api.splitbill.dto.groups.GroupsEntityDto;
import com.api.splitbill.dto.groups.GroupsSaveDto;

import java.util.List;

public interface IGroupService {
    GroupsEntityDto saveGroup(Integer uniqueId, GroupsSaveDto group) throws KnownException;

    List<GroupsEntityDto> getAllGroups(Integer uniqueId);
}
