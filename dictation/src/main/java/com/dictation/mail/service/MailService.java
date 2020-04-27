package com.dictation.mail.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.List;

/**
 * @ClassName MailService
 * @Description
 * @Author zlc
 * @Date 2020-04-23 20:56
 */
@Service
public class MailService {

    @Autowired
    JavaMailSenderImpl javaMailSender;


    @Async("asyncServiceExecutor")
    public void send(List<File> fileList , String id,String content,String title) throws MessagingException {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "utf-8");
        fileList.forEach(f -> {
            try {
                mimeMessageHelper.addAttachment(f.getName(),f);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        });
        mimeMessageHelper.setText("来自用户id：" + id + "<br><br>" + content, true);
        mimeMessageHelper.setSubject(title);
        mimeMessageHelper.setTo("dictationmail@163.com");
        mimeMessageHelper.setFrom("390597658@qq.com");
        try {
            javaMailSender.send(mimeMessage);
        } catch (MailException e) {
            e.printStackTrace();
        }finally {
            fileList.forEach(file -> file.delete());
        }


    }


}
