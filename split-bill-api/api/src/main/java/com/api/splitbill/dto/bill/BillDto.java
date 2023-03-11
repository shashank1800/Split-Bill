package com.api.splitbill.dto.bill;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class BillDto{
    Integer id;
    Integer groupId;
    String name;
    Float totalAmount;
    Long dateCreated;
    Integer uniqueId;
    List<BillShareDetailDto> billShares;
}