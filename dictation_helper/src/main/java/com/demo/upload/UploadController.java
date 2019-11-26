package com.demo.upload;


import com.jfinal.core.Controller;
import com.jfinal.upload.UploadFile;

import java.io.IOException;

public class UploadController extends Controller {


    QiniuService qiniuService = new QiniuService();


    public void index(){
        redirect("upload.html");
    }

    public void testUpload(){
        UploadFile file = getFile();
        try {
            String url = qiniuService.saveImage(file);
            System.out.println("success: imageUrl = "+url);
            render(url);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
