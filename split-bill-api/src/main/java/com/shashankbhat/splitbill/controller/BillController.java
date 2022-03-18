package com.shashankbhat.splitbill.controller;

import com.shashankbhat.splitbill.dto.bill.BillAllDto;
import com.shashankbhat.splitbill.dto.bill.BillDto;
import com.shashankbhat.splitbill.dto.bill.BillSaveDto;
import com.shashankbhat.splitbill.dto.bill.BillShareEntityDto;
import com.shashankbhat.splitbill.entity.BillEntity;
import com.shashankbhat.splitbill.entity.BillShareEntity;
import com.shashankbhat.splitbill.entity.UsersEntity;
import com.shashankbhat.splitbill.repository.BillRepository;
import com.shashankbhat.splitbill.repository.BillShareRepository;
import com.shashankbhat.splitbill.repository.LoggedUsersRepository;
import com.shashankbhat.splitbill.repository.UsersRepository;
import com.shashankbhat.splitbill.util.HelperMethods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/bill")
public class BillController {

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private BillShareRepository billShareRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private LoggedUsersRepository loggedUsersRepository;

    @PostMapping(value = "/saveBill")
    public ResponseEntity<BillSaveDto> saveBill(@RequestBody BillSaveDto transaction) {

        Integer uniqueId = HelperMethods.getUniqueId(loggedUsersRepository);

        transaction.getBill().setDateCreated(System.currentTimeMillis());
        transaction.getBill().setUniqueId(uniqueId);
        transaction.getBill().setId(null);
        BillEntity billEntity = billRepository.save(transaction.getBill());

        transaction.getBillShares().forEach(billShareEntity -> {
            billShareEntity.setBillId(billEntity.getId());
            billShareEntity.setDateCreated(System.currentTimeMillis());
            billShareEntity.setUniqueId(uniqueId);
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
        List<UsersEntity> users = usersRepository.findByGroupId(groupId);

        Map<Integer, UsersEntity> usersEntityMap = new HashMap<>();
        users.forEach(usersEntity -> {
            usersEntityMap.put(usersEntity.getId(), usersEntity);
        });


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

            List<BillShareEntityDto> billSharesEntity = billShares.stream().map(billShareEntity -> new BillShareEntityDto(
                    billShareEntity.getId(),
                    billShareEntity.getBillId(),
                    billShareEntity.getUserId(),
                    null,
                    billShareEntity.getSpent(),
                    billShareEntity.getShare(),
                    billShareEntity.getDateCreated()
            )).collect(Collectors.toList());
            for (int i = 0; i < billSharesEntity.size(); i++) {
                billSharesEntity.get(i).setUserId(billShares.get(i).getUserId());
                billSharesEntity.get(i).setUser(usersEntityMap.get(billShares.get(i).getUserId()));
            }

            bill.setBillShares(billSharesEntity);

            allBill.add(bill);

        });
        billAllDto.setData(allBill);
        return new ResponseEntity<>(billAllDto, HttpStatus.OK);
    }

    @PutMapping(value = "/deleteBills")
    public ResponseEntity<BillDto> deleteBills(@RequestBody BillDto billDto) throws Exception {
        Integer uniqueId = HelperMethods.getUniqueId(loggedUsersRepository);
        BillEntity billEntity = billRepository.findOneById(billDto.getId());

        if(!Objects.equals(uniqueId, billEntity.getUniqueId()))
            throw new Exception("Unauthorized Exception");

        billDto.getBillShares().forEach(billShareEntityDto -> {
            billShareRepository.deleteById(billShareEntityDto.getId());
        });

        billRepository.deleteById(billDto.getId());
        return new ResponseEntity<>(billDto, HttpStatus.OK);
    }
}
