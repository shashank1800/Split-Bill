package com.shashankbhat.splitbill.dto.bill;

import com.shashankbhat.splitbill.entity.BillEntity;
import com.shashankbhat.splitbill.entity.BillShareEntity;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class BillSaveDetailDto {
    BillEntity bill;
    List<BillShareEntity> billShares;
}