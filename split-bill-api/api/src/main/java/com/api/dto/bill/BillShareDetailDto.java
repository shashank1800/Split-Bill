package com.api.dto.bill;

import com.api.dto.user.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BillShareDetailDto {
    Integer id;
    Integer billId;
    Integer userId;
    UserDto user;
    Float spent;
    Float share;
    Long dateCreated;
}