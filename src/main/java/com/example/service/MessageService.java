package com.example.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;

@Service
@Transactional
public class MessageService {
    private MessageRepository messageRepository;
    private AccountRepository accountRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository, AccountRepository accountRepository) {
        this.messageRepository = messageRepository;
        this.accountRepository = accountRepository;
    }

    public Message findMessageByPostedBy(Message message) {
        Integer postedBy = message.getPostedBy();
        Optional<Message> messageOptional = messageRepository.findMessageByPostedBy(postedBy);
        if(messageOptional.isPresent()) {
            return messageOptional.get();
        }
        return null;
    }

    public List<Message> getAllMessagesFromAccountByAccountId(Integer accountId) {
        // Find the account by accountId
        if (!accountRepository.existsById(accountId)) {
            throw new IllegalArgumentException("Account with ID " + accountId + " does not exist.");
        }

        return messageRepository.findAllMessagesByPostedBy(accountId);
    }

    public Message createMessage(Message message) {
        // Message Successful Only If:
        // - messageText is not blank && is not over 255 characters
        String messageText = message.getMessageText();
        // - postedBy refers to a real, existing user
        Message existingUser = findMessageByPostedBy(message);

        if(!messageText.isBlank() && messageText.length() < 255 && existingUser != null) {
            Message createMessage = messageRepository.save(message);
            return createMessage;
        }
        return null;
    }

    public List<Message> getAllMessagesLsit() {
        return (List<Message>) messageRepository.findAll();
    }

    public Message getMessageByMessageId(Integer messageId) {
        Optional<Message> messageOptional = messageRepository.findById(messageId);

        if (messageOptional.isPresent()) {
            Message message = messageOptional.get();
            return message;
        }
        return null;
    }

    public int deleteMessageByMessageId(Integer messageId) {
        int rowsDeleted = messageRepository.deleteByMessageIdAndReturnCount(messageId);
        return rowsDeleted;
    }

    public int patchMessageTextByMessageId(Integer messageId, String newMessageText) {
        System.out.println(newMessageText);
        if(newMessageText == null ||  newMessageText.isBlank() || newMessageText.length() > 255 || newMessageText.isEmpty() || newMessageText == "") {
            System.out.println("We're returning 0");
            return 0; 
        }

        Message existingMessage = getMessageByMessageId(messageId);

        if(existingMessage != null ) {
            existingMessage.setMessageText(newMessageText);
            messageRepository.save(existingMessage);
            return 1;
        }
        return 0;
    }


}
