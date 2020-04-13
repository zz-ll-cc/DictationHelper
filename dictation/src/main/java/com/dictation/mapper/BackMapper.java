package com.dictation.mapper;



import com.dictation.back.entity.Back;

import java.util.List;


public interface BackMapper{

    public List<Back> findAll();

    public int insert(Back back);

}
