package com.dictation.user.controller;

import com.dictation.user.entity.*;
import com.dictation.user.service.UserService;
import com.dictation.util.RedisUtil;
import com.dictation.util.TimeUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @ClassName: UserController
 * @Description: TODO
 * @Author: szy
 * @Date 2020/4/14
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    RedisUtil redisUtil;

    @RequestMapping("/login")
    public LoginInfo login(@RequestParam(value = "login_type",required = true)int login_type,
                        @RequestParam(value = "phone",required = true,defaultValue = "")String phone,
                        @RequestParam(value = "password",required = false)String password){
        LoginInfo loginInfo = null;
        switch (login_type) {
            case 1:
                loginInfo = this.loginByVC(phone);   // 使用验证码登陆
                break;
            case 2:
                loginInfo = this.loginUserByPwd(phone, password);    // 使用密码登录
                break;
        }
        return loginInfo;
    }
    /**
    * @Description: 使用密码登陆
    *
    * @return: com.dictation.user.entity.User
    * @Author: szy
    */
    private LoginInfo loginUserByPwd(String phone,String password) {
        LoginInfo loginInfo=new LoginInfo();
        Boolean info = userService.checkUser(phone);
        if (info){
            User user= this.userService.loginByPP(phone,password);
            if (user == null){
                loginInfo.setRegister_type(3);
            }else {
                loginInfo.setUser(user);
                loginInfo.setRegister_type(1);
            }
        }else {
            loginInfo.setRegister_type(2);
        }
        return loginInfo;
    }

    /**
     * 验证码登录
     */
    public LoginInfo loginByVC(String phone) {
        LoginInfo loginInfo=new LoginInfo();
        Boolean info = userService.checkUser(phone);
        System.out.println(info);
        if (info){
            User user = userService.findUserByPhone(phone);
            loginInfo.setRegister_type(2);
            loginInfo.setUser(user);
        }
        else {
            userService.saveUser(phone);
            User user = userService.findUserByPhone(phone);
            loginInfo.setRegister_type(1);
            loginInfo.setUser(user);
        }
        return loginInfo;
    }

    /**
     * 更新 user 信息
     * @return
     */
    @RequestMapping("/updateuser")
    public User updateUser(@RequestBody User user){
        System.out.println(user.toString());
        return this.userService.updateUser(user);
    }

    /**
     * 修改头像
     */
    @RequestMapping("/uploadhead")
    public User uploadhead(@RequestParam(value = "fileUrl",required = false) String fileUrl,
                           @RequestParam(value = "uid",required = false) int uid){
        // 1. 修改头像地址
        this.userService.updateUserImage(uid,fileUrl);
        // 2. 返回user对象
        User user = this.userService.findUserByUid(uid);
        return user;
    }

    /**
    * @Description: 修改密码
    * @Param:  * @param type 0：为拥有密码时设置密码，1：通过旧密码修改密码，2：通过手机验证修改密码
     * @param uid   userid
     * @param passwordOld  旧密码
     * @param passwordNew  新密码
     * @param password      设置的密码
    * @return: com.dictation.user.entity.LoginInfo
    * @Author: szy
    * @date: 2020/4/15
    */
    @RequestMapping("/updatepwd")
    public LoginInfo updatepwd(@RequestParam(value = "type" , required = true,defaultValue = "")int type,
                               @RequestParam(value = "uid",required = true,defaultValue = "")int uid,
                               @RequestParam(value = "upasswordOld",required = false)String passwordOld,
                               @RequestParam(value = "upasswordNew",required = false)String passwordNew,
                               @RequestParam(value = "upassword",required = false)String password) {
        LoginInfo loginInfo = new LoginInfo();
        if (type == 1) {    // 此时为根据旧密码修改旧密码
            System.out.println("旧"+passwordOld+",新"+passwordNew);
            String phone = this.userService.findPhoneByUid(uid);
            User user = userService.loginByPP(phone, passwordOld);
            if (user != null) {
                user = userService.updatePwd(user, passwordNew);
                loginInfo.setRegister_type(1);
                loginInfo.setUser(user);
            } else {
                loginInfo.setRegister_type(0);
            }
        } else if(type == 0 || type == 2){    // 此时为设置密码
            System.out.println(password);
            User user = userService.findUserByUid(uid);
            user = userService.updatePwd(user, password);
            loginInfo.setRegister_type(1);
            loginInfo.setUser(user);
        }
        return loginInfo;
    }

    /**
     * 删除数据库用户
     */
    @RequestMapping("/deleteUser")
    public String deleteUser(@RequestBody User user){
        this.userService.deleteUser(user);
        return "success";
    }


    /**
     *
     * 若不传year，默认为今年
     *
     * @param id
     * @param year 不是必传字段
     * @return
     */
    @RequestMapping("/check")
    public UserSignIn getSignInMap(@RequestParam("id") int id, @RequestParam(value = "year", required = false) String... year){
        UserSignIn signIn = new UserSignIn();
        signIn.setUser(userService.findUserByUid(id)).setYearRecord(userService.getSignInRecordMap(id,year));

//        String key = redisUtil.createUserSignInKey(id,year);
//        UserSignIn signIn = new UserSignIn();
//        try {
//            signIn
//                    .setWeekRecord(redisUtil.getBitList(key))
//                    .setContinuousSignIn(userService.getContinuousSignIn(id))
//                    .setUser(new ObjectMapper().readValue((String)redisUtil.get(redisUtil.getUserKey(id)),User.class));
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
        return signIn;
    }






    /**
     * 用户签到,调用signIn
     * @param id
     * @return  如果签到失败，返回null，否则返回对应user
     */
    @RequestMapping("/signIn")
    public User signIn(@RequestParam("id") int id){

        return userService.signIn(id);

//        String key = redisUtil.createUserSignInKey(id,null);
//        int dayOfWeek = TimeUtil.calculateDayOfWeek();
//        long time = 0;
//        if(!redisUtil.hasKey(key)){
//            time = TimeUtil.getSecondsToNextMonday4pm();
//        }
//        if(!redisUtil.getBit(key,dayOfWeek-1)){
//            //检查是否有连续登陆记录，如果有，刷新时间，incr，如果没有，返回1，创建一天记录，设置时间
//            redisUtil.setBit(key,dayOfWeek-1,true, time);
//            //异步
//            userService.updateUserCreditAndInsertRecordAsync(id,"每日登录",5);
//        }
//        UserSignIn signIn = new UserSignIn();
//        try {
//            signIn
//                    .setWeekRecord(redisUtil.getBitList(key))
//                    .setContinuousSignIn(userService.continuousSignIn(id))
//                    .setUser(new ObjectMapper().readValue((String)redisUtil.get(redisUtil.getUserKey(id)),User.class));
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
//        return signIn;
    }


    /**
     * 更新用户信息
     *
     * 从缓存中获取用户，如果没有，则查找数据库并更新lastLoginTime然后存入缓存
     *
     * 记录app日活
     *
     * @param id
     * @return
     */
    @RequestMapping("/updatemyself")
    public User updateMySelf(@RequestParam("id") int id){
        User user;
        user = userService.findUserByUid(id);
        user = userService.recordLastLoginTime(user);
        userService.recordActiveUser(id);
        return user;
    }


    /**
     * 2：有效听写，
     * 3：学习10分钟 4：学习30分钟 5：学习60分钟
     * @param id
     * @return
     */
    @RequestMapping("updateCredit")
    public User modifyCredit(@RequestParam("id") int id, @RequestParam("code") int code){
        String reason  = "";
        int creditNum = 0;
        switch (code){
            case 1:
                reason = ReasonEnum.DT.getReason();
                creditNum = ReasonEnum.DT.getCreditNum();
                break;
            case 2:
                reason = ReasonEnum.STM.getReason();
                creditNum = ReasonEnum.STM.getCreditNum();
                break;
            case 3:
                reason = ReasonEnum.SHH.getReason();
                creditNum = ReasonEnum.SHH.getCreditNum();
                break;
            case 4:
                reason = ReasonEnum.SAH.getReason();
                creditNum = ReasonEnum.SAH.getCreditNum();
                break;
        }
        System.out.println(creditNum);
        return userService.updateUserCreditAndInsertRecord(id,reason,creditNum);
    }




    @RequestMapping("/reSignIn")
    public User reSignIn(@RequestParam("id") int id, @RequestParam("date") String date){
        //不用判断日期是否是格式化的！！！
        return userService.reSignIn(id, date) ? userService.findUserByUid(id) : null;
    }






}
