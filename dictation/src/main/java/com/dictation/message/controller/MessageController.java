package com.dictation.message.controller;

import com.dictation.message.entity.Message;
import com.dictation.message.service.MessageService;
import com.dictation.util.FileUtil;
import com.dictation.util.QiniuUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;


/**
 * @ClassName MessageController
 * @Description
 * @Author zlc
 * @Date 2020-05-21 16:29
 */
@Controller
@RequestMapping("/message")
public class MessageController {


    @Autowired
    QiniuUtil qiniuUtil;

    @Autowired
    MessageService messageService;


    @RequestMapping("/index")
    public String index(){
        return "message";
    }

    @RequestMapping("/publish")
    public String publish(@RequestParam("titleImage") MultipartFile titleImage,
                          @RequestParam("title") String title,
                          @RequestParam("subtitle")String subtitle,
                          @RequestParam("content") String content,
                          @RequestParam("typeName") String typeName){
        File f = null;
        InputStream ins = null;
        try {
            ins = titleImage.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        f = new File(titleImage.getOriginalFilename());
        FileUtil.inputStreamToFile(ins,f);
        String url = null;
        try {
            url = qiniuUtil.saveImage(f,titleImage.getOriginalFilename());
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("success: imageUrl = "+url);
        f.delete();
        Message message = new Message();
        message.setTitleImage(url).setTitle(title).setSubtitle(subtitle).setContent(content).setTypeName(typeName);
        messageService.publishMessage(message);
        return "message-publish";

    }



    @RequestMapping("/readMessage")
    @ResponseBody
    public String readMessage(@RequestParam("userId") Integer uid, @RequestParam("messageId") Integer mid){
        messageService.messageRead(uid,mid);
        return "ok";
    }




    @RequestMapping("/all")
    @ResponseBody
    public Map getAllMessage(@RequestParam(value = "version",required = false) Integer version){
        Map<String,Object> map = new HashMap<>();
        if(version == null){
            map.put("status",0);//代表客户端第一次获取
        }else{
            if(version == messageService.getMessageVersion()){
                map.put("status",1);//代表版本一致
                return map;
            }else{
                map.put("status",2);//代表版本更新了
            }
        }
        map.put("MessageList",messageService.findAllMessage());
        map.put("MessageVersion",messageService.getMessageVersion());
        return map;
    }




}
