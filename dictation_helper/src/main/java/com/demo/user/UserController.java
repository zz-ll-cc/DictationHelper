package com.demo.user;

import com.google.gson.JsonObject;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;

public class UserController extends Controller {

    @Inject
    UserService userService;

    public void index(){
        redirect("userhome.html");
    }



    public void registUserByNP(){
        String name = get("name");
        String phone = get("phone");
        String password = get("password");
        renderJson(userService.registByPassword(name,phone,password));
    }


    public void loginUserByPP(){
        String phone = get("phone");
        String password = get("password");
        renderJson(userService.loginByPP(phone,password));
    }

}
