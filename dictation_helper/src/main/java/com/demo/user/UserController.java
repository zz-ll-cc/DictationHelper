package com.demo.user;

import com.demo.common.model.TblUser;
import com.demo.entity.LoginInfo;
import com.demo.upload.QiniuService;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.kit.HttpKit;
import com.jfinal.kit.JsonKit;
import com.jfinal.upload.UploadFile;

import java.io.BufferedReader;
import java.io.IOException;

public class UserController extends Controller {

    @Inject
    UserService userService;
    QiniuService qiniuService=new QiniuService();
//    public void index() {
//        redirect("/user/userhome.html");
//    }
    public void registUserByNP() {
        String name = get("name");
        String phone = get("phone");
        String password = get("password");
        renderJson(userService.registByPassword(name, phone, password));
    }
    public void loginUserByPP() {
        String phone = get("phone");
        String password = get("password");
        renderJson(userService.loginByPP(phone, password));
    }

    /**
     * 登录
     */
    public void login(){

        String phone=getPara("phone");
        int login_type = getInt("login_type");
        //System.out.println(phone);
        System.out.println(":"+login_type);
        if(login_type==1) {
            this.loginByVC(phone);
        }
        else{
            String password=get("password");
            this.loginUserByPwd(phone,password);
        }

    }

    /**
     * 密码登录
     */
    private void loginUserByPwd(String phone,String password) {
        LoginInfo loginInfo=new LoginInfo();
        Boolean info = userService.checkPhone(phone);
        if (info){
            TblUser user= userService.loginByPP(phone,password);
            if (user == null){
                loginInfo.setRegister_type(3);
            }else {
                loginInfo.setUser(user);
                loginInfo.setRegister_type(1);
            }
        }else {
            loginInfo.setRegister_type(2);
        }
        renderJson(loginInfo);
    }

    /**
     * 验证码登录
     */
    public void loginByVC(String phone) {
        LoginInfo loginInfo=new LoginInfo();
        Boolean info = userService.checkPhone(phone);
        if (info){
            TblUser user = userService.findUserByPhone(phone);
            loginInfo.setRegister_type(2);
            loginInfo.setUser(user);
        }
        else {
            userService.saveUser(phone);
            TblUser user = userService.findUserByPhone(phone);
            loginInfo.setRegister_type(1);
            loginInfo.setUser(user);
        }
        renderJson(loginInfo);

    }
    /**
     * 更新user
     * @return
     */
    public void updateuser(){
        String json=HttpKit.readData(getRequest());
        System.out.println(json);
        //String json=get("json");
        //System.out.println(json);
        TblUser user= JsonKit.parse(json,TblUser.class);
        System.out.println(user);
        renderJson(userService.updateUser(user));
    }
    /**
     * 修改头像
     */
    public void uploadhead(){
        UploadFile file = getFile();
        int uid=getInt("uid");
        System.out.println(uid);
        try {
            String url = qiniuService.saveImage(file);
            System.out.println("success: imageUrl = "+url);
            TblUser user=userService.findUserByUid(uid);
            System.out.println("user"+user);
            user.set("uheadPath",url);
            file.getFile().delete();
            //userService.updateUser(user);
            userService.updateHead(user);
            renderJson(userService.findUserByUid(uid));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新密码
     */
    public void updatepwd(){
        TblUser tblUser=new TblUser();
        LoginInfo loginInfo=new LoginInfo();
        int type=getInt("type");
        if (type==1){
            String passwordOld=get("upasswordOld");
            String passwordNew=get("upasswordNew");
            int uid=getInt("uid");
            TblUser user=userService.findUserByUid(uid);
            String phone=user.getUphone();
            user=userService.loginByPP(phone,passwordOld);
            System.out.println(user);
            if (user!=null){
                tblUser=userService.updatePwd(user,passwordNew);
                loginInfo.setRegister_type(1);
                loginInfo.setUser(tblUser);
            }else {
                loginInfo.setRegister_type(0);
            }
        }else {
            String password=get("upassword");
            int uid=getInt("uid");
            TblUser user=userService.findUserByUid(uid);
            tblUser=userService.updatePwd(user,password);
            loginInfo.setRegister_type(1);
            loginInfo.setUser(tblUser);
        }
        renderJson(loginInfo);
    }

}
