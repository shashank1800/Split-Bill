package com.shashankbhat.splitbill.database.local.dto.groups;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupsSaveDto {
    @NotEmpty(message = "Group name cannot be empty")
    public String name;
}