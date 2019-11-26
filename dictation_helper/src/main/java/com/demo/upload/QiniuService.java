package com.demo.upload;


import com.alibaba.fastjson.JSONObject;
import com.demo.utils.FileUtil;
import com.jfinal.upload.UploadFile;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;
import java.util.logging.Logger;

public class QiniuService {


    // 设置好账号的ACCESS_KEY和SECRET_KEY
    String accessKey = "h8iBKUOEWkHhWCDV8R8R9Ls8YTB8s_cxxkqpkU-Z";
    String secretKey = "ZAatbabppO2bZeLcr4-FL8N-GA7kPAbVtmI4obxK";
    String bucket = "testqnycc";
    //    七牛默认域名
    String domain = "http://q1kb2gx86.bkt.clouddn.com";


    // 密钥配置
    Auth auth = Auth.create(accessKey, secretKey);
    // 构造一个带指定Zone对象的配置类,不同的七云牛存储区域调用不同的zone
    Configuration cfg = new Configuration(Zone.zone1());
    // ...其他参数参考类注释
    UploadManager uploadManager = new UploadManager(cfg);

    // 简单上传，使用默认策略，只需要设置上传的空间名就可以了
    public String getUpToken() {
        return auth.uploadToken(bucket);
    }


    public String saveImage(UploadFile file) throws IOException {
        try {
            int dotPos = file.getFileName().lastIndexOf(".");
            if (dotPos < 0) {
                return null;
            }
            String fileExt = file.getFileName().substring(dotPos + 1).toLowerCase();
            // 判断是否是合法的文件后缀
            if (!FileUtil.isFileAllowed(fileExt)) {
                return null;
            }

            String fileName = UUID.randomUUID().toString().replaceAll("-", "") + "." + fileExt;

            //转字节数组
            byte[] buffer = null;
            FileInputStream fis = new FileInputStream(file.getFile());
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while((n = fis.read(b)) != -1){
                bos.write(b,0,n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();

            Response res = uploadManager.put(buffer, fileName, getUpToken());
            // 打印返回的信息
            if (res.isOK() && res.isJson()) {
                // 返回这张存储照片的地址
                return domain + JSONObject.parseObject(res.bodyString()).get("key");
            } else {
                System.out.println("七牛异常:" + res.bodyString());
                return null;
            }
        } catch (QiniuException e) {
            // 请求失败时打印的异常的信息
            System.out.println("七牛异常:" + e.getMessage());
            return null;
        }
    }







}
