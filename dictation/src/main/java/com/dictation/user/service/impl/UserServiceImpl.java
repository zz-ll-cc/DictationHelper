package com.dictation.user.service.impl;


import com.dictation.mapper.UserMapper;
import com.dictation.user.entity.User;
import com.dictation.user.service.UserService;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Random;

/**
 * @ClassName: UserServiceImpl
 * @Description: TODO
 * @Author: szy
 * @Date 2020/4/14
 */
@Service("userServiceImpl")
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;
    static final String SALT = "haohaoxuexi";

    public boolean checkUser(String phone){
        User user = this.userMapper.checkPhone(phone);
        if (user == null){
            return false;
        }else return true;
    }

    @Override
    public String findPhoneByUid(int uid) {
        return this.userMapper.findPhoneByUid(uid);
    }

    @Override
    public User findUserByPhone(String phone) {
        return this.userMapper.findUserByPhone(phone);
    }

    @Override
    public User findUserByUid(int uid) {
        return this.userMapper.findUserByUid(uid);
    }

    @Override
    public void saveUser(String phone) {
        String name= getStringRandom();
        User user = new User();
        user.setUphone(phone);
        user.setUname(name);
        this.userMapper.saveUser(user);
    }

    @Override
    public void updateUserImage(int id, String url) {
        this.userMapper.updateUserImage(id,url);
    }

    @Override
    public User loginByPP(String phone, String password) {
        // 1.将传来的pwd解码
        byte[] b = Base64.getDecoder().decode(password.getBytes());
        String upassword = new String(b);
        // 2.将密码二次加密
        String firstEnc = DigestUtils.md5Hex(upassword);
        String secondEnc = DigestUtils.md5Hex(firstEnc+SALT);
        return this.userMapper.loginByPP(phone,secondEnc);
    }

    @Override
    // TODO: 2020/4/15 向服务器上传头像
    public void uploadHead(User user) {

    }

    @Override
    public User updateUser(User user) {
        String upassword = user.getUpassword();
        if (upassword != null) {
            byte[] b = Base64.getDecoder().decode(upassword.getBytes());
            String password = new String(b);
            String firstEncrypt = DigestUtils.md5Hex(password);
            String secondEncrypt = DigestUtils.md5Hex(firstEncrypt + SALT);
            user.setUpassword(secondEncrypt);
        }
        this.userMapper.updateUser(user);
        user = this.userMapper.findUserByUid(user.getUid());
        return user;
    }

    @Override
    public User updatePwd(User user, String upassword) {
        if (upassword != null) {
            byte[] b = Base64.getDecoder().decode(upassword.getBytes());
            String password = new String(b);
            String firstEncrypt = DigestUtils.md5Hex(password);
            String secondEncrypt = DigestUtils.md5Hex(firstEncrypt + SALT);
            this.userMapper.updatePwd(user.getUid(),secondEncrypt);
            user.setUpassword(secondEncrypt);
        }
        return user;
    }

    @Override
    public void deleteUser(User user) {
        this.userMapper.deleteUser(user.getUid());
    }

    /**
     * 随机生成姓名
     */
    public static String getStringRandom() {
        int length=12;
        String val = "";
        Random random = new Random();

        //参数length，表示生成几位随机数
        for(int i = 0; i < length; i++) {

            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            //输出字母还是数字
            if( "char".equalsIgnoreCase(charOrNum) ) {
                //输出是大写字母还是小写字母
                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
                val += (char)(random.nextInt(26) + temp);
            } else if( "num".equalsIgnoreCase(charOrNum) ) {
                val += String.valueOf(random.nextInt(10));
            }
        }
        return val;
    }
}
