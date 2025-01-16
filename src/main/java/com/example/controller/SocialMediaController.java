package com.example.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.entity.*;
import com.example.service.AccountService;
import com.example.service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */

// Because I'm using ResponseEntity, Spring Boot assumes a serialized object in the body into JSON; otherwise use @RestController
@Controller
public class SocialMediaController {
    private AccountService accountService;
    private MessageService messageService;

    @Autowired
    public SocialMediaController(AccountService accountService, MessageService messageService) {
        this.accountService = accountService;
        this.messageService = messageService;
    }

    // POST localhost:8080/register
    @PostMapping("/register")
    public ResponseEntity<Account> registerAccount(@RequestBody Account account) {
        // Check for duplicate username; respond with 409 if duplicate found
        Optional<Account> isDuplicateAccountUsernameOptional = accountService.findAccountByUsername(account);
        if(isDuplicateAccountUsernameOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(account);
        }

        // Attempt to register account; respond with 200 if fine
        Account savedAccount = accountService.registerAccount(account);
        if(savedAccount != null) {
            return ResponseEntity.status(HttpStatus.OK).body(savedAccount);
        } 

        // Otherwise for all other requests; respond with 400
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(account);
    }

    // POST localhost:8080/login
    @PostMapping("/login")
    public ResponseEntity<Account> loginAccount(@RequestBody Account account) {
        // Attempt to login with creds
        Account verifyAccount = accountService.loginAccount(account);
        
        // Account authorized & granted access; respond with 200
        if(verifyAccount != null) {
            return ResponseEntity.status(HttpStatus.OK).body(verifyAccount);
        }
        
        // Otherwise respond with 401
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(verifyAccount);
    }

    // POST localhost:8080/messages
    @PostMapping("messages")
    public ResponseEntity<Message> createMessage(@RequestBody Message message) {

        Message createdMessage = messageService.createMessage(message);

        // Message created; respond with 200
        if(createdMessage != null) {
            return ResponseEntity.status(HttpStatus.OK).body(createdMessage);
        }

        // Otherwise, respond with 400 (Client Error)
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createdMessage);
    }

    // GET localhost:8080/messages
    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getAllMessagesList() {
        List<Message> allMessageList = messageService.getAllMessagesLsit();
        return ResponseEntity.ok(allMessageList);
    }

    // GET localhost:8080/messages/{messageId}
    @GetMapping("/messages/{messageId}")
    public ResponseEntity<Message> getMessageByMessageId(@PathVariable Integer messageId) {
        Message message = messageService.getMessageByMessageId(messageId);
        
        return ResponseEntity.status(HttpStatus.OK).body(message);
    }

    // DELETE localhost:8080/messages/{messageId}
    @DeleteMapping("messages/{messageId}")
    public ResponseEntity<Integer> deleteMessageByMessageId(@PathVariable Integer messageId) {
        int rowsDeleted = messageService.deleteMessageByMessageId(messageId);

        if(rowsDeleted > 0) {
            return ResponseEntity.status(HttpStatus.OK).body(rowsDeleted);
        } else {
            return ResponseEntity.ok().build();
        }
    }

    // PATCH localhost:8080/messages/{messageId}
    @PatchMapping("messages/{messageId}")
    public  ResponseEntity<Integer> patchMessageTextByMessageId(@PathVariable Integer messageId, @RequestBody String newMessageText) {
        // The request body should contain a new messageText values to replace the message identified by messageId
        int rowsAffected = messageService.patchMessageTextByMessageId(messageId, newMessageText);
        

        if(rowsAffected > 0) {
            return ResponseEntity.ok(rowsAffected);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

}
