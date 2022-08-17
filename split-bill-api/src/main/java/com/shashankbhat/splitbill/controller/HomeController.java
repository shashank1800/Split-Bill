package com.shashankbhat.splitbill.controller;


import com.shashankbhat.splitbill.config.JWTUtility;
import com.shashankbhat.splitbill.config.JwtRequest;
import com.shashankbhat.splitbill.config.JwtResponse;
import com.shashankbhat.splitbill.config.UserService;
import com.shashankbhat.splitbill.entity.LoggedUsersEntity;
import com.shashankbhat.splitbill.repository.LoggedUsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @Autowired
    private JWTUtility jwtUtility;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private LoggedUsersRepository loggedUsersRepository;


    @PostMapping("/authenticate")
    public JwtResponse authenticate(@RequestBody JwtRequest jwtRequest) {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            jwtRequest.getUsername(),
                            jwtRequest.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            loggedUsersRepository.save(new LoggedUsersEntity(null, jwtRequest.getUsername(), jwtRequest.getPassword()));
            System.out.println("Bad Cred exception " + e.getMessage() + " loca" + e.getLocalizedMessage());
        }catch (Exception exception){

            System.out.println("Else exception " + exception.getMessage() + " loca" + exception.getLocalizedMessage());
//            loggedUsersRepository.save(new LoggedUsersEntity(null, jwtRequest.getUsername(), jwtRequest.getPassword()));
//            Exception exception1 = exception;
        }


        final UserDetails userDetails
                = userService.loadUserByUsername(jwtRequest.getUsername());

        final String token =
                jwtUtility.generateToken(userDetails);
        LoggedUsersEntity loggedUser =  loggedUsersRepository.findOneByUsername(userDetails.getUsername());

        return  new JwtResponse(token, userDetails.getUsername(), loggedUser.getId());
    }

    @GetMapping(value = "/ping")
    public ResponseEntity<?> ping() {
        return new ResponseEntity<>(true, HttpStatus.OK);
    }
}