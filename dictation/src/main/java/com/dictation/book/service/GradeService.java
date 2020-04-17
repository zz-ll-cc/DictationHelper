package com.dictation.book.service;

import com.dictation.book.entity.Grade;
import com.dictation.book.entity.Word;

import java.util.List;

public interface GradeService {

    List<Grade> findAll();
    boolean save(Grade grade);
    boolean delete(Grade grade);
    boolean delete(Integer id);
    boolean update(Grade grade);

}
