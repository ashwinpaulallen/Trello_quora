package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.upgrad.quora.service.exception.AuthorizationFailedException;git
import com.upgrad.quora.service.exception.InvalidQuestionException;
import com.upgrad.quora.service.exception.UserNotFoundException;

import java.time.ZonedDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/")
public class QuestionController {


    @Autowired
    private QuestionBusinessService questionBusinessService;

    @Autowired
    private UserAuthBusinessService userAuthBusinessService;

    @RequestMapping(method = RequestMethod.POST, path = "/question/create", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<QuestionResponse> createQuestion(@RequestHeader("authorization") final String authorization, final QuestionRequest questionRequest) throws AuthorizationFailedException {

        final UserAuthEntity userAuthEntity = userAuthBusinessService.getUser(authorization);

        QuestionEntity questionEntity = new QuestionEntity();
        questionEntity.setUuid(UUID.randomUUID().toString());
        questionEntity.setContent(questionRequest.getContent());
        questionEntity.setUser(userAuthEntity.getUser());
        final ZonedDateTime now = ZonedDateTime.now();
        questionEntity.setDate(now);

        final QuestionEntity createdQuestion = questionBusinessService.createQuestion(questionEntity, userAuthEntity);
        QuestionResponse questionResponse = new QuestionResponse().id(createdQuestion.getUuid()).status("QUESTION CREATED");

        return new ResponseEntity<QuestionResponse>(questionResponse, HttpStatus.CREATED);
    }

    // This endpoint is used to fetch all the questions that have been posted in the application by any user.
    // It used the GET request
    //if user is signedout throws AuthorizationFailedException
    @RequestMapping(method = RequestMethod.GET, path = "/question/all", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<QuestionDetailsResponse>> getAllQuestion(@RequestHeader("authorization") final String authorization) throws AuthorizationFailedException {

        final UserAuthEntity userAuthEntity = userAuthBusinessService.getUser(authorization);

        final List<QuestionEntity> allQuestion = questionBusinessService.getAllQuestion(userAuthEntity);

        List<QuestionDetailsResponse> questionResponse = questionslist(allQuestion);

        return new ResponseEntity<List<QuestionDetailsResponse>>(questionResponse, HttpStatus.OK);
    }

    //This endpoint is used to fetch all the questions posed by a specific user
    //this is a GET request
    //throws user not found exception if user is not found
    @RequestMapping(method = RequestMethod.GET, path = "/question/all/{userId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<QuestionDetailsResponse>> getAllQuestionsByUser(@PathVariable("userId") final String userId, @RequestHeader("authorization") final String authorization)
            throws AuthorizationFailedException, UserNotFoundException {

        final UserAuthEntity userAuthEntity = userAuthBusinessService.getUser(authorization);

        final List<QuestionEntity> allQuestionByUser = questionBusinessService.getAllQuestionsByUser(userId, userAuthEntity);

        return new ResponseEntity<List<QuestionDetailsResponse>>(questionslist(allQuestionByUser), HttpStatus.OK);

    }

    //This endpoint is used to edit a question that has been posted by a user.
    //it is a PUT request
    @RequestMapping(method = RequestMethod.PUT , path = "/question/edit/{questionId}" ,consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ResponseEntity<QuestionEditResponse> editQuestionContent(@PathVariable("questionId") final String questionId , @RequestHeader("authorization") final String authorization, QuestionEditRequest questionEditRequest)
            throws AuthorizationFailedException,InvalidQuestionException {

        final UserAuthEntity userAuthEntity = userAuthBusinessService.getUser(authorization);
        String content = questionEditRequest.getContent();

        QuestionEntity editedQuestion = questionBusinessService.editQuestionContent(questionId,userAuthEntity, content);
        QuestionEditResponse questionEditResponse = new QuestionEditResponse().id(editedQuestion.getUuid()).status("QUESTION EDITED");

        return new ResponseEntity<QuestionEditResponse>(questionEditResponse,HttpStatus.OK);
    }

    //This endpoint is used to delete a question that has been posted by a user
    //this is a DELETE request
    @RequestMapping(method = RequestMethod.DELETE , path = "/question/delete/{questionId}" ,  produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<QuestionDeleteResponse> deleteQuestion(@RequestHeader("authorization") final String authorization, @PathVariable("questionId") final String questionid )
            throws AuthorizationFailedException , InvalidQuestionException {

        final UserAuthEntity userAuthEntity = userAuthBusinessService.getUser(authorization);


        QuestionEntity deletedQuestion = questionBusinessService.deleteQuestion(questionid, userAuthEntity);
        QuestionDeleteResponse questionDeleteResponse = new QuestionDeleteResponse().id(deletedQuestion.getUuid()).status("QUESTION DELETED");

        return new ResponseEntity<QuestionDeleteResponse>(questionDeleteResponse, HttpStatus.OK);

    }

    public List<QuestionDetailsResponse> questionslist(List<QuestionEntity> allQuestion){
        List<QuestionDetailsResponse> listofquestions = new ArrayList<>();
        for ( QuestionEntity questionEntity : allQuestion){
            QuestionDetailsResponse Response = new QuestionDetailsResponse();
            Response.id(questionEntity.getUuid());
            Response.content(questionEntity.getContent());
            listofquestions.add(Response);
        }
        return listofquestions;
    }
}