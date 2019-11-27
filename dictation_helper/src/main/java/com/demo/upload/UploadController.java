package com.demo.upload;


import com.demo.book.BookService;
import com.demo.word.WordService;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.upload.UploadFile;

import java.io.IOException;

public class UploadController extends Controller {

    @Inject
    BookService bookService;

    @Inject
    WordService wordService;

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
            int bid = getInt("bid");
            bookService.update(bid,url);
            if(file.getFile().delete()){
                System.out.println("已经删除本地文件,修改数据库成功");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        redirect("uploadFinish.html");
    }

    public void uploadWordImage(){
        UploadFile file = getFile();
        try {
            String url = qiniuService.saveImage(file);
            System.out.println("success: imageUrl = "+url);
            int wid = getInt("wid");
            wordService.update(wid,url);
            if(file.getFile().delete()){
                System.out.println("已经删除本地文件,修改数据库成功");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        redirect("uploadFinish.html");
    }

}
