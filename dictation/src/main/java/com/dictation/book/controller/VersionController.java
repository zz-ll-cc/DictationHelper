package com.dictation.book.controller;

import com.dictation.book.entity.Version;
import com.dictation.book.service.VersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName BookVersionController
 * @Description
 * @Author zlc
 * @Date 2020-04-14 13:58
 */
@RestController
@RequestMapping("/version")
public class VersionController {

    @Autowired
    VersionService versionService;

    @RequestMapping("/all")
    public List<Version> all(){
        return versionService.findAll();
    }


}
