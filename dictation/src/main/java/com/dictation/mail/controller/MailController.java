package com.dictation.mail.controller;

import com.dictation.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
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
    JavaMailSenderImpl javaMailSender;

    @RequestMapping("/report")
    public String sendReportMail(@RequestParam("id") String id,@RequestParam("title") String title,
                                 @RequestParam("content") String content, @RequestParam("files") MultipartFile[] files){

        try {
            List<File> fileList = null;
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "utf-8");
            mimeMessageHelper.setText("来自用户id：" + id + "<br><br>" + content, true);
            mimeMessageHelper.setSubject(title);
            mimeMessageHelper.setTo("dictationmail@163.com");
            mimeMessageHelper.setFrom("390597658@qq.com");
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
                        mimeMessageHelper.addAttachment(f.getName(),f);
                        fileList.add(f);
                    }
                }
            }
            javaMailSender.send(mimeMessage);
            fileList.forEach(file -> file.delete());
            return "发送成功";
        } catch (MessagingException | IOException e) {
            e.printStackTrace();
            return "服务器正忙，请稍后再试";
        }


    }






}
