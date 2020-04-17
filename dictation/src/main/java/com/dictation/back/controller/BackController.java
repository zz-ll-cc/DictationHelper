package com.dictation.back.controller;

import com.dictation.back.entity.Back;
import com.dictation.back.service.BackService;
import com.dictation.util.QiniuUtil;
import com.sun.xml.internal.bind.v2.TODO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @ClassName BackController
 * @Description
 * @Author zlc
 * @Date 2020-04-15 15:28
 */
@RestController
@RequestMapping("/back")
public class BackController {

    @Autowired
    BackService backService;

    @Autowired
    QiniuUtil qiniuUtil;


    /**
     * TODO
     * @return
     */
    @RequestMapping("/saveback")
    public Back saveBack(){


        return null;
    }




}
