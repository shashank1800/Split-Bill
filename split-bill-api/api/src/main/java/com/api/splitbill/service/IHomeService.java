package com.api.splitbill.service;

import com.common.exception.KnownException;
import com.api.splitbill.config.JwtRequest;
import com.api.splitbill.config.JwtResponse;

public interface IHomeService {

    JwtResponse authenticate(JwtRequest jwtRequest) throws KnownException;

}
