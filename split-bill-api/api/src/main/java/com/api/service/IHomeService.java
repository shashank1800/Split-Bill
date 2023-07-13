package com.api.service;

import com.common.exception.KnownException;
import com.api.config.jwt.JwtRequest;
import com.api.config.jwt.JwtResponse;

public interface IHomeService {

    JwtResponse authenticate(JwtRequest jwtRequest) throws KnownException;

}
