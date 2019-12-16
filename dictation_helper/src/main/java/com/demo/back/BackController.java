package com.demo.back;

import com.demo.common.model.TblBack;
import com.demo.upload.QiniuService;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.upload.UploadFile;

import java.io.IOException;
import java.util.Date;

public class BackController extends Controller {
    @Inject
    private BackService backService;
    @Inject
    private QiniuService qiniuService;
    public void saveback(){
        int img_status=getInt("img");
        Date date=getDate("time");
        TblBack back=new TblBack();
        if (img_status==1){
            try {
                UploadFile file = getFile();
                String url = qiniuService.saveImage(file);
                System.out.println("success: imageUrl = "+url);
                int uid = getInt("uid");
                String type=get("type");
                String info=get("info");
                back.set("info",info).set("type",type).set("uid",uid).set("time",date).set("status",0).set("path",url);
                backService.saveBack(back);
                if(file.getFile().delete()){
                    System.out.println("已经删除本地文件,修改数据库成功");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            int uid = getInt("uid");
            String type=get("type");
            String info=get("info");
            back.set("info",info).set("type",type).set("uid",uid).set("time",date).set("status",0);
            backService.saveBack(back);
        }
        renderJson(back);
    }

}
