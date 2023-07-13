package com.api.controller;

import com.api.config.jwt.JwtRequest;
import com.api.config.jwt.JwtResponse;
import com.api.service.IHomeService;
import com.common.exception.KnownException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @Autowired
    private IHomeService homeService;

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody JwtRequest jwtRequest) {
        try {
            JwtResponse response = homeService.authenticate(jwtRequest);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (KnownException kn) {
            return ResponseEntity.badRequest().body(kn.getErrorMessage());
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping(value = "/ping")
    public ResponseEntity<?> ping() {
        return new ResponseEntity<>(true, HttpStatus.OK);
    }
}