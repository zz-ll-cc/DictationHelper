package com.demo.version;

import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;

public class VersionController extends Controller {
    @Inject
    private VersionService versionService;
    public void all(){
        renderJson(versionService.findAllVersion());
    }
}
