package com.api.service;

import com.common.exception.KnownException;
import com.api.config.JwtRequest;
import com.api.config.JwtResponse;

public interface IHomeService {

    JwtResponse authenticate(JwtRequest jwtRequest) throws KnownException;

}
