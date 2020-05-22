package com.dictation.message.service;

import com.dictation.message.entity.Message;
import com.dictation.message.entity.MessageRecord;

import java.util.List;

public interface MessageService {

    //发布一个消息
    boolean publishMessage(Message message);


    //删除一个消息
    boolean deletedMessage(Integer id);

    boolean deletedMessage(Message message);

    //修改一个消息
    boolean updateMessage(Message message);


    //查询全部消息
    List<Message> findAllMessage();

    List<Message> findAllMessageByPaging(int pageSize,int pageNum);


    Integer getMessageVersion();


    //文章被一个用户访问时
    boolean messageRead(Integer uid,Integer mid);

    //查询用户读过哪些消息
    List<MessageRecord> findUserRecord(Integer uid);








}
