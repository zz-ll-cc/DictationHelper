package com.dictation.back.service;

import com.dictation.back.entity.Back;

import java.util.List;


public interface BackService {

    Back saveOne(Back back);

    boolean deleteById(Integer id);

    boolean delete(Back back);

    boolean updateOne(Back back);

    Back findById(Integer id);


    List<Back> findAll();


}
