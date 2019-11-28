package com.demo.unit;

import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;

public class UnitController extends Controller {
    @Inject
    private UnitService unitService;
    public void all(){
        renderJson(unitService.findAll());
    }
}
