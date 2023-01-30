package com.shashankbhat.splitbill.dto.groups;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupsSaveDto {
    @NotEmpty(message = "Group name cannot be empty")
    public String name;
    public List<Integer> peoples;
}