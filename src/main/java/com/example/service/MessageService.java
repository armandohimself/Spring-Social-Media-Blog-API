package com.example.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Message;
import com.example.repository.MessageRepository;

@Service
public class MessageService {
    private MessageRepository messageRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public Message findMessageByPostedBy(Message message) {
        Integer postedBy = message.getPostedBy();
        Optional<Message> messageOptional = messageRepository.findMessageByPostedBy(postedBy);
        if(messageOptional.isPresent()) {
            return messageOptional.get();
        }
        return null;
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
}
