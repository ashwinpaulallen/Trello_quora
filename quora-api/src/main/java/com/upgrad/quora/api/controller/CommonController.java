package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.UserDetailsResponse;
import com.upgrad.quora.service.business.UserAuthBusinessService;
import com.upgrad.quora.service.business.UserBusinessService;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class CommonController {

    @Autowired
    private UserBusinessService userBusinessService;

    @Autowired
    private UserAuthBusinessService userAuthBusinessService;

    //This endpoint is used to get the details of any user in the Quora Application
    //This is a GET request
    //throws AuthorizationFailedException, if the access token provided by the user does not exist in the database
    //throws AuthorizationFailedException, If the user has signed out
    //throws UserNotFoundException, If the user with uuid whose profile is to be retrieved does not exist in the database
    @RequestMapping(method = RequestMethod.GET, path = "/userprofile/{userId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<UserDetailsResponse> userProfile(@PathVariable("userId") final String uuid,
                                                           @RequestHeader("authorization") final String authorization) throws AuthorizationFailedException, UserNotFoundException {
        final UserAuthEntity userAuthEntity = userAuthBusinessService.getUser(authorization);
        UserEntity userEntity = userBusinessService.userProfile(uuid,userAuthEntity);

        UserDetailsResponse userDetailsResponse = new UserDetailsResponse().firstName(userEntity.getFirstname())
                .lastName(userEntity.getLastname()).emailAddress(userEntity.getEmail())
                .contactNumber(userEntity.getMobile()).dob(userEntity.getDob()).aboutMe(userEntity.getAboutme())
                .country(userEntity.getCountry()).userName(userEntity.getUsername());

        return new ResponseEntity<UserDetailsResponse>(userDetailsResponse, HttpStatus.OK);
    }
}
