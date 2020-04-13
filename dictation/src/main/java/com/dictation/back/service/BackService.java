package com.dictation.back.service;

import com.dictation.back.entity.Back;

import java.util.List;

/**
 * @ClassName BackService
 * @Description
 * @Author zlc
 * @Date 2020-04-13 11:28
 */
public interface BackService {

    public Back saveOne(Back back);


    public List<Back> findAll();


}
