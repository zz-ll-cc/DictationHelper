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



    }


    public void uploadBookImage(){
        UploadFile file = getFile();
        try {
            String url = qiniuService.saveImage(file);
            System.out.println("success: imageUrl = "+url);
            String bid = get("bid");
            if(file.getFile().delete()){
                System.out.println("已经删除本地文件");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        renderHtml("uploadFinish.html");
    }

    public void uploadWordImage(){
        UploadFile file = getFile();
        try {
            String url = qiniuService.saveImage(file);
            System.out.println("success: imageUrl = "+url);
            String bid = get("bid");
            if(file.getFile().delete()){
                System.out.println("已经删除本地文件");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        renderHtml("uploadFinish.html");
    }

}
