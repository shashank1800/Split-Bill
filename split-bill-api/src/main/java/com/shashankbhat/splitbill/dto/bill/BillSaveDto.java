package com.shashankbhat.splitbill.dto.bill;

import com.shashankbhat.splitbill.entity.BillEntity;
import com.shashankbhat.splitbill.entity.BillShareEntity;
import lombok.Data;
import java.util.List;

@Data
public class BillSaveDto {
    BillEntity bill;
    List<BillShareEntity> billShares;
}