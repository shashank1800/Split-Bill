package com.shashankbhat.splitbill.service;

import com.shashankbhat.splitbill.dto.groups.GroupsEntityDto;
import com.shashankbhat.splitbill.dto.groups.GroupsSaveDto;
import com.shashankbhat.splitbill.exception.KnownException;

import java.util.List;

public interface IGroupService {
    GroupsEntityDto saveGroup(Integer uniqueId, GroupsSaveDto group) throws KnownException;

    List<GroupsEntityDto> getAllGroups(Integer uniqueId);
}
