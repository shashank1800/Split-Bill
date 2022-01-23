package com.shashankbhat.splitbill.dto.bill;

import com.shashankbhat.splitbill.entity.BillShareEntity;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class BillDto{
    Integer id;
    Integer groupId;
    String name;
    Float totalAmount;
    Long dateCreated;
    List<BillShareEntity> billShares;
}