package com.dictation.message.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dictation.mapper.MessageMapper;
import com.dictation.mapper.MessageRecordMapper;
import com.dictation.message.entity.Message;
import com.dictation.message.entity.MessageRecord;
import com.dictation.message.service.MessageService;
import com.dictation.util.RedisUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName MessageServiceImpl
 * @Description
 * @Author zlc
 * @Date 2020-05-21 15:27
 */
@Service("messageService")
public class MessageServiceImpl implements MessageService {


    @Autowired
    MessageMapper messageMapper;

    @Autowired
    MessageRecordMapper messageRecordMapper;

    @Autowired
    RedisUtil redisUtil;

    private Logger logger = LoggerFactory.getLogger(this.getClass());


    @Override
    public boolean publishMessage(Message message) {
        messageMapper.insert(message);
        String key = redisUtil.getMessageKey();
        String versionKey = redisUtil.getMessageVersionKey();
        message.setDeleted(0);

        if(redisUtil.hasKey(key) && redisUtil.hasKey(versionKey)){
            JavaType javaType = new ObjectMapper().getTypeFactory().constructCollectionType(List.class,Message.class);
            List<Message> messageList;
            try {
                messageList = new ObjectMapper().readValue((String) redisUtil.get(key),javaType);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return false;
            }
            messageList.add(0,message);
            redisUtil.incr(versionKey,1);
            try {
                redisUtil.set(key,new ObjectMapper().writeValueAsString(messageList));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }else if(!(redisUtil.hasKey(key) && redisUtil.hasKey(versionKey))){
            //key不存在
            List<Message> messageList = new ArrayList<>();
            messageList.add(message);
            try {
                redisUtil.set(key,new ObjectMapper().writeValueAsString(messageList));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return false;
            }
            redisUtil.set(versionKey,1);
            return true;
        }else{
            logger.warn("缓存中消息key缺失");
            return false;
        }


    }

    @Override
    public boolean deletedMessage(Integer id) {
        messageMapper.deleteById(id);
        return true;
    }

    @Override
    public boolean deletedMessage(Message message) {
        messageMapper.deleteById(message.getId());
        return true;
    }

    @Override
    public boolean updateMessage(Message message) {
        messageMapper.updateById(message);
        return true;
    }

    @Override
    public List<Message> findAllMessage() {
        String key = redisUtil.getMessageKey();
        if (redisUtil.hasKey(key)) {
            //如果key存在
            JavaType javaType = new ObjectMapper().getTypeFactory().constructCollectionType(List.class,Message.class);
            List<Message> messageList = null;
            try {
                messageList = new ObjectMapper().readValue((String) redisUtil.get(key),javaType);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return messageList;
        }else{
            QueryWrapper<Message> wrapper = new QueryWrapper<>();
            wrapper.orderByDesc("create_time");
            List<Message> messages = messageMapper.selectList(wrapper);
            if(messages != null && messages.size() > 0){
                try {
                    redisUtil.set(key,new ObjectMapper().writeValueAsString(messages));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
                redisUtil.set(redisUtil.getMessageVersionKey(),1);
            }
            return messages;
        }
    }


    /**
     * 按时间降序排列
     * @param pageSize
     * @param pageNum
     * @return
     */
    @Override
    public List<Message> findAllMessageByPaging(int pageSize, int pageNum) {
        Page<Message> messagePage = new Page<>(pageNum,pageSize);
        QueryWrapper<Message> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("create_time");
        return messageMapper.selectPage(messagePage,wrapper).getRecords();
    }

    @Override
    public Integer getMessageVersion() {
        return (Integer) redisUtil.get(redisUtil.getMessageVersionKey());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean messageRead(Integer uid, Integer mid) {
        //在记录表中留下记录，修改文章的访问次数
        QueryWrapper<MessageRecord> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id",uid).eq("message_id",mid);
        MessageRecord messageRecord = messageRecordMapper.selectOne(wrapper);
        if(messageRecord == null){
            messageRecord = new MessageRecord();
            messageRecord.setMessageId(mid).setUserId(uid);
            messageRecordMapper.insert(messageRecord);
        }else{
            messageRecord.setHits(messageRecord.getHits()+1);
            messageRecordMapper.updateById(messageRecord);
        }
        Message message = messageMapper.selectById(mid);
        message.setHits(message.getHits()+1);
        messageMapper.updateById(message);
        return false;
    }

    @Override
    public List<MessageRecord> findUserRecord(Integer uid) {
        QueryWrapper<MessageRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",uid);
        return messageRecordMapper.selectList(queryWrapper);
    }
}
