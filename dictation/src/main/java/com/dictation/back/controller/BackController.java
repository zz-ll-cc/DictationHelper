package com.dictation.back.controller;

import com.dictation.back.service.BackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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




}
