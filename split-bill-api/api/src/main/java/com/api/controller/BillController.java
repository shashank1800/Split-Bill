package com.api.controller;

import com.api.service.IBillService;
import com.common.exception.KnownException;
import com.shashankbhat.repository.LoggedUsersRepository;
import com.api.dto.bill.BillAllDto;
import com.api.dto.bill.BillDto;
import com.api.dto.bill.BillSaveDetailDto;
import com.api.dto.bill.BillSaveDto;
import com.shashankbhat.util.HelperMethods;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bill")
@Api("Bills")
public class BillController {

    @Autowired
    private LoggedUsersRepository loggedUsersRepository;

    @Autowired
    private IBillService billService;

    @ApiOperation(value = "To save bills", response = BillSaveDetailDto.class)
    @PostMapping(value = "/saveBill")
    public ResponseEntity<?> saveBill(@RequestBody BillSaveDto transaction) {

        try{
            Integer uniqueId = HelperMethods.getUniqueId(loggedUsersRepository);

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

        try {
            Integer uniqueId = HelperMethods.getUniqueId(loggedUsersRepository);

            BillDto result = billService.deleteBills(billDto, uniqueId);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (KnownException kn) {
            return ResponseEntity.badRequest().body(kn.getErrorMessage());
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
