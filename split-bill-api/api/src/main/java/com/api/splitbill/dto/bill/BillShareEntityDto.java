package com.api.splitbill.dto.bill;

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
    Float spent;
    Float share;
    Long dateCreated;
    Integer uniqueId;
}