package com.dictation.book.service;

import com.dictation.book.entity.Grade;
import com.dictation.book.entity.Version;

import java.util.List;

public interface VersionService {

    boolean save(Version version);
    boolean delete(Version version);
    boolean delete(Integer id);
    boolean update(Version version);

    Version findById(int bvId);

    List<Version> findAll();

    List<Version> findAllByPaging(int pageNum, int pageSize);
}
