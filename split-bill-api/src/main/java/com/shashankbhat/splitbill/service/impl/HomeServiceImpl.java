package com.shashankbhat.splitbill.service.impl;


import com.shashankbhat.entity.LoggedUsersEntity;
import com.shashankbhat.exception.KnownException;
import com.shashankbhat.repository.LoggedUsersRepository;
import com.shashankbhat.splitbill.config.JWTUtility;
import com.shashankbhat.splitbill.config.JwtRequest;
import com.shashankbhat.splitbill.config.JwtResponse;
import com.shashankbhat.splitbill.config.UserService;
import com.shashankbhat.splitbill.service.IHomeService;
import com.shashankbhat.util.Valid;
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
            loggedUsersRepository.save(loggedUsersEntityValid.getValue());
//            System.out.println("Else exception " + exception.getMessage() + " loca" + exception.getLocalizedMessage());
//            Exception exception1 = exception;
        }

        final UserDetails userDetails = userService.loadUserByUsername(jwtRequest.getUsername());
        final String token = jwtUtility.generateToken(userDetails);

        LoggedUsersEntity loggedUser =  loggedUsersRepository.findOneByUsername(userDetails.getUsername());

        return new JwtResponse(token, userDetails.getUsername(), loggedUser.getId());
    }

}
