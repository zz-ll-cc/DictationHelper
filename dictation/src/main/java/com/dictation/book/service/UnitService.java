package com.dictation.book.service;

import com.dictation.book.entity.Grade;
import com.dictation.book.entity.Unit;

import java.util.List;

public interface UnitService {

    List<Unit> findAll();
    Unit findOneById(Integer id);
    boolean save(Unit unit);
    boolean delete(Unit unit);
    boolean delete(Integer id);
    boolean update(Unit unit);

}

