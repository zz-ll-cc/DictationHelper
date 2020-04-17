package com.dictation.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dictation.book.entity.Version;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface VersionMapper extends BaseMapper<Version> {
}
