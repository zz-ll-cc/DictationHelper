package com.dictation.book.controller;

import com.dictation.book.entity.Unit;
import com.dictation.book.service.UnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName UnitController
 * @Description
 * @Author zlc
 * @Date 2020-04-14 14:30
 */
@RestController
@RequestMapping("/unit")
public class UnitController {

    @Autowired
    UnitService unitService;

    @RequestMapping("/all")
    public List<Unit> all(){
        return unitService.findAll();
    }


}
