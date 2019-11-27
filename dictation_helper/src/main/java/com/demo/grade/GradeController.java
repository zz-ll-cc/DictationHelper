package com.demo.grade;

import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;

public class GradeController extends Controller {
    @Inject
    private GradeService gradeService;

    public void all(){
        renderJson(gradeService.findAll());
    }
}
