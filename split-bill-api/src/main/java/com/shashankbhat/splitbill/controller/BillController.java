package com.shashankbhat.splitbill.controller;

import com.shashankbhat.splitbill.dto.bill.BillAllDto;
import com.shashankbhat.splitbill.dto.bill.BillDto;
import com.shashankbhat.splitbill.dto.bill.BillSaveDto;
import com.shashankbhat.splitbill.entity.BillEntity;
import com.shashankbhat.splitbill.entity.BillShareEntity;
import com.shashankbhat.splitbill.repository.BillRepository;
import com.shashankbhat.splitbill.repository.BillShareRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/bill")
public class BillController {

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private BillShareRepository billShareRepository;


    @PostMapping(value = "/saveBill")
    public ResponseEntity<BillSaveDto> saveBill(@RequestBody @Valid BillSaveDto transaction) {
        transaction.getBill().setDateCreated(System.currentTimeMillis());
        BillEntity billEntity = billRepository.save(transaction.getBill());

        transaction.getBillShares().forEach(billShareEntity ->{
            billShareEntity.setBillId(billEntity.getId());
            billShareEntity.setDateCreated(System.currentTimeMillis());
            BillShareEntity billShareModel = billShareRepository.save(billShareEntity);

            billShareEntity.setId(billShareModel.getId());
        });

        transaction.getBill().setId(billEntity.getId());

        return new ResponseEntity<>(transaction, HttpStatus.OK);
    }

    @GetMapping(value = "/getBills")
    public ResponseEntity<?> getAllBills(@RequestParam Integer groupId) {

        BillAllDto billAllDto = new BillAllDto();

        List<BillEntity> allBillEntity = billRepository.findAllByGroupId(groupId);

        List<BillDto> allBill = new ArrayList<>();
        allBillEntity.forEach(billEntity -> {

            BillDto bill = new BillDto(
                    billEntity.getId(),
                    billEntity.getGroupId(),
                    billEntity.getName(),
                    billEntity.getTotalAmount(),
                    billEntity.getDateCreated(),
                    null
            );

            List<BillShareEntity> billShares = billShareRepository.findAllByBillId(billEntity.getId());
            bill.setBillShares(billShares);

            allBill.add(bill);

        });
        billAllDto.setData(allBill);
        return new ResponseEntity<>(billAllDto, HttpStatus.OK);
    }
}
