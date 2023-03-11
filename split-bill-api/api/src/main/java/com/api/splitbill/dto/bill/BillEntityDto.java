package com.api.splitbill.dto.bill;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class BillEntityDto {

    Integer id;
    Integer groupId;
    String name;
    Float totalAmount;
    Long dateCreated;
    Integer uniqueId;


}