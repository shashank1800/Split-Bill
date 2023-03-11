package com.api.service.impl;


import com.api.config.JWTUtility;
import com.shashankbhat.entity.LoggedUsersEntity;
import com.common.exception.KnownException;
import com.shashankbhat.repository.LoggedUsersRepository;
import com.api.config.JwtRequest;
import com.api.config.JwtResponse;
import com.api.config.UserService;
import com.api.service.IHomeService;
import com.common.util.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class HomeServiceImpl implements IHomeService {

    @Autowired
    private JWTUtility jwtUtility;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private LoggedUsersRepository loggedUsersRepository;

    @Override
    public JwtResponse authenticate(JwtRequest jwtRequest) throws KnownException {

        Valid<LoggedUsersEntity> loggedUsersEntityValid = LoggedUsersEntity.create(null, jwtRequest.getUsername(),
                jwtRequest.getPassword(), System.currentTimeMillis());

        if (loggedUsersEntityValid.isFailed())
            throw new KnownException(loggedUsersEntityValid.getMessage());

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            jwtRequest.getUsername(),
                            jwtRequest.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            loggedUsersRepository.save(loggedUsersEntityValid.getValue());
//            System.out.println("Bad Cred exception " + e.getMessage() + " loca" + e.getLocalizedMessage());
        } catch (Exception exception){
//            loggedUsersRepository.save(loggedUsersEntityValid.getValue());
//            System.out.println("Else exception " + exception.getMessage() + " loca" + exception.getLocalizedMessage());
//            Exception exception1 = exception;
        }

        final UserDetails userDetails = userService.loadUserByUsername(jwtRequest.getUsername());
        final String token = jwtUtility.generateToken(userDetails);

        LoggedUsersEntity loggedUser =  loggedUsersRepository.findOneByUsername(userDetails.getUsername());

        return new JwtResponse(token, userDetails.getUsername(), loggedUser.getId());
    }

}
