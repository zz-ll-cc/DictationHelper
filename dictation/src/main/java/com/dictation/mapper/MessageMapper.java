package com.dictation.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dictation.message.entity.Message;
import org.springframework.stereotype.Component;

@Component
public interface MessageMapper extends BaseMapper<Message> {
}
