package com.shashankbhat.splitbill.service;

import com.shashankbhat.splitbill.config.JwtRequest;
import com.shashankbhat.splitbill.config.JwtResponse;
import com.shashankbhat.splitbill.exception.KnownException;

public interface IHomeService {

    JwtResponse authenticate(JwtRequest jwtRequest) throws KnownException;

}
