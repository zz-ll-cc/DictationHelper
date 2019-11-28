package com.demo.upload;


import com.demo.book.BookService;
import com.demo.user.UserService;
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

    @Inject
    UserService userService;

    QiniuService qiniuService = new QiniuService();


    public void index(){
        redirect("upload.html");
    }

    public void testUpload(){



    }


    /**
     * 上传书的封面
     */
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

    /**
     * 上传单词的配图
     */
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


    /**
     * 修改用户头像
     */
    public void uploadUserImage(){
        UploadFile file = getFile();
        try {
            String url = qiniuService.saveImage(file);
            System.out.println("success: imageUrl = "+url);
            int uid = getInt("uid");
            userService.updateUserImage(uid,url);
            if(file.getFile().delete()){
                System.out.println("已经删除本地文件,修改数据库成功");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        redirect("uploadFinish.html");
    }

}
