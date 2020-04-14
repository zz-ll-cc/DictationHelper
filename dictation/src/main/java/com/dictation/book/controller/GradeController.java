package com.dictation.book.controller;

import com.dictation.book.entity.Grade;
import com.dictation.book.service.GradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName GradeController
 * @Description
 * @Author zlc
 * @Date 2020-04-14 14:28
 */
@RestController
@RequestMapping("/grade")
public class GradeController {

    @Autowired
    GradeService gradeService;

    @RequestMapping("/all")
    public List<Grade> all(){
        return gradeService.findAll();
    }


}
