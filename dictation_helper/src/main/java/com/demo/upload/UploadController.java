package com.demo.upload;

import com.jfinal.core.Controller;

public class UploadController extends Controller {


    public void index(){
        redirect("upload.html");
    }

    public void testUpload(){
        renderJson("hihihihihi");
    }

}
