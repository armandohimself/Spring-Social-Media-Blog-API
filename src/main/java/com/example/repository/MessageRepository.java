package com.example.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.entity.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer>{

    public Optional<Message> findMessageByPostedBy(Integer postedBy);

    @Modifying
    @Transactional
    @Query("DELETE FROM Message WHERE messageId = :messageId")
    public int deleteByMessageIdAndReturnCount(@Param("messageId") Integer messageId);

    
    @Query("SELECT m FROM Message m WHERE m.postedBy = :accountId")
    public List<Message> findAllMessagesByPostedBy(@Param("accountId") Integer accountId);
}
