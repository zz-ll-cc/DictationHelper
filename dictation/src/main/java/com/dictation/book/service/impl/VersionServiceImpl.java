package com.dictation.book.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dictation.book.entity.Version;
import com.dictation.book.service.VersionService;
import com.dictation.mapper.VersionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName BookVersionServiceImpl
 * @Description
 * @Author zlc
 * @Date 2020-04-13 14:49
 */
@Service("bookVersionService")
public class VersionServiceImpl implements VersionService {

    @Autowired
    VersionMapper versionMapper;

    @Override
    public boolean save(Version version) {
        return versionMapper.insert(version) == 1;
    }

    @Override
    public boolean delete(Version version) {
        return versionMapper.deleteById(version.getId()) == 1;
    }

    @Override
    public boolean delete(Integer id) {
        return versionMapper.deleteById(id) == 1;
    }

    @Override
    public boolean update(Version version) {
        return versionMapper.updateById(version) == 1;
    }

    @Override
    public Version findById(int bvId) {
        return versionMapper.selectById(bvId);
    }

    @Override
    public List<Version> findAll() {
        return versionMapper.selectList(null);
    }

    @Override
    public List<Version> findAllByPaging(int pageNum, int pageSize) {
        Page<Version> versionPage = new Page<>(pageNum,pageSize);
        return versionMapper.selectPage(versionPage,null).getRecords();
    }
}
