package com.shashankbhat.splitbill.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping(value = "/ping")
    public ResponseEntity<?> ping() {
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

}
