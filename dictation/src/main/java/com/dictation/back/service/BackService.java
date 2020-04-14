package com.dictation.back.service;

import com.dictation.back.entity.Back;

import java.util.List;


public interface BackService {

    public Back saveOne(Back back);


    public List<Back> findAll();


}
