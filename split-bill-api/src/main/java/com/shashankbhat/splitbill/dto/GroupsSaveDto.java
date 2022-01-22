package com.shashankbhat.splitbill.dto;


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