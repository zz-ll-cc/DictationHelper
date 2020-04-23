package com.dictation.mail.controller;

import com.dictation.mail.service.MailService;
import com.dictation.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName MailController
 * @Description
 * @Author zlc
 * @Date 2020-04-22 17:04
 */
@RestController
@RequestMapping("/mail")
public class MailController {



    @Autowired
    MailService mailService;

    @RequestMapping("/report")
    public String sendReportMail(@RequestParam("id") String id,@RequestParam("title") String title,
                                 @RequestParam("content") String content, @RequestParam("files") MultipartFile[] files){
        try {
            List<File> fileList = null;
            if(files.length > 0){
                fileList = new ArrayList<>(files.length);
                for(MultipartFile file : files){
                    String fileExt = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1).toLowerCase();
                    // 判断是否是合法的文件后缀
                    if (FileUtil.isFileAllowed(fileExt)) {
                        File f = null;
                        InputStream ins = file.getInputStream();
                        f = new File(file.getOriginalFilename());
                        FileUtil.inputStreamToFile(ins,f);
                        fileList.add(f);
                    }
                }
            }
            mailService.send(fileList,id,content,title);
        } catch (MessagingException | IOException e) {
            e.printStackTrace();
        }

        return "发送成功";
    }






}
