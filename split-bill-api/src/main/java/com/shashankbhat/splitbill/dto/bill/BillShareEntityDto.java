package com.shashankbhat.splitbill.dto.bill;

import com.shashankbhat.splitbill.entity.UsersEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BillShareEntityDto {
    Integer id;
    Integer billId;
    Integer userId;
    UsersEntity user;
    Float spent;
    Float share;
    Long dateCreated;
}