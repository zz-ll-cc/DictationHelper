package com.dictation.upload.controller;

import com.dictation.book.service.BookService;
import com.dictation.book.service.WordService;
import com.dictation.util.FileUtil;
import com.dictation.util.QiniuUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * @ClassName UploadController
 * @Description
 * @Author zlc
 * @Date 2020-04-15 15:36
 */
@Controller
@RequestMapping("/upload")
public class UploadController {

    @Autowired
    QiniuUtil qiniuUtil;

    @Autowired
    BookService bookService;

    @Autowired
    WordService wordService;


    @RequestMapping("/index")
    public String index(){
        return "upload";
    }


    @RequestMapping("/uploadBookImage")
    public String uploadBookImage(@RequestParam("file") MultipartFile file , @RequestParam("bid") int bid) throws IOException {
        if (file.isEmpty()) {
            System.out.println("上传失败，请选择文件");
            return "uploadBookImage";
        }
        File f = null;
        InputStream ins = file.getInputStream();
        f = new File(file.getOriginalFilename());
        FileUtil.inputStreamToFile(ins,f);
        String url = qiniuUtil.saveImage(f,file.getOriginalFilename());
        System.out.println("success: imageUrl = "+url);
        if(bookService.updateUrl(bid,url) == null) System.out.println("更新失败");
        f.delete();

        return "uploadFinish";
    }

    //用户头像上传没写


    @RequestMapping("/uploadWordImage")
    public String uploadWordImage(@RequestParam("file") MultipartFile file , @RequestParam("wid") int wid) throws IOException{
        if (file.isEmpty()) {
            System.out.println("上传失败，请选择文件");
            return "uploadWordImage";
        }
        File f = null;
        InputStream ins = file.getInputStream();
        f = new File(file.getOriginalFilename());
        FileUtil.inputStreamToFile(ins,f);
        String url = qiniuUtil.saveImage(f,file.getOriginalFilename());
        System.out.println("success: imageUrl = "+url);
        if(!wordService.updatePic(wid,url)) System.out.println("更新失败");
        f.delete();
        return "uploadFinish";
    }






}
