package com.shashankbhat.splitbill.controller;

import com.shashankbhat.splitbill.dto.bill.*;
import com.shashankbhat.splitbill.exception.KnownException;
import com.shashankbhat.splitbill.repository.LoggedUsersRepository;
import com.shashankbhat.splitbill.service.IBillService;
import com.shashankbhat.splitbill.util.HelperMethods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bill")
public class BillController {

    @Autowired
    private LoggedUsersRepository loggedUsersRepository;

    @Autowired
    private IBillService billService;

    @PostMapping(value = "/saveBill")
    public ResponseEntity<?> saveBill(@RequestBody BillSaveDto transaction) {

        Integer uniqueId = HelperMethods.getUniqueId(loggedUsersRepository);

        try{
            BillSaveDetailDto result = billService.saveBill(transaction, uniqueId);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (KnownException kn){
            return ResponseEntity.badRequest().body(kn.getErrorMessage());
        } catch (Exception ex){
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping(value = "/getBills")
    public ResponseEntity<?> getAllBills(@RequestParam Integer groupId) {

        try{
            BillAllDto result = billService.getAllBills(groupId);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (KnownException kn){
            return ResponseEntity.badRequest().body(kn.getErrorMessage());
        } catch (Exception ex){
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping(value = "/deleteBills")
    public ResponseEntity<?> deleteBills(@RequestBody BillDto billDto) {
        Integer uniqueId = HelperMethods.getUniqueId(loggedUsersRepository);

        try {
            BillDto result = billService.deleteBills(billDto, uniqueId);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (KnownException kn) {
            return ResponseEntity.badRequest().body(kn.getErrorMessage());
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
