package com.api.dto.bill;

import com.shashankbhat.entity.BillEntity;
import com.shashankbhat.entity.BillShareEntity;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class BillSaveDetailDto {
    BillEntity bill;
    List<BillShareEntity> billShares;
}