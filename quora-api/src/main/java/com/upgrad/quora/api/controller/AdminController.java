package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.UserDeleteResponse;
import com.upgrad.quora.service.business.UserBusinessService;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class AdminController {

    @Autowired
    private UserBusinessService userBusinessService;

    //This endpoint is used to delete a user from the Quora Application
    //it is a a DELETE request
    //throws AuthorizationFailedException, if the access token provided by the user does not exist in the database
    //throws AuthorizationFailedException, if the role of the user is 'nonadmin'
    @RequestMapping(method = RequestMethod.DELETE, path = "/admin/user/{userId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<UserDeleteResponse> deleteUser(@PathVariable("userId") final String uuid,
                                                         @RequestHeader("authorization") final String authorization) throws AuthorizationFailedException, UserNotFoundException {
        userBusinessService.deleteUser(uuid,authorization);

        UserDeleteResponse userDeleteResponse = new UserDeleteResponse().id(uuid).status("USER SUCCESSFULLY DELETED");
        return new ResponseEntity<UserDeleteResponse>(userDeleteResponse, HttpStatus.OK);
    }
}
