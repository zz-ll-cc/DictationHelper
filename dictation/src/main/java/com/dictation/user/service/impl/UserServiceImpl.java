package com.dictation.user.service.impl;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.dictation.mapper.UserMapper;
import com.dictation.user.entity.User;
import com.dictation.user.service.UserService;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;
import java.util.Random;
import java.util.function.Predicate;

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

    @Override
    public boolean checkUser(String phone) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(User::getUid).eq(User::getUphone, phone);
        return this.userMapper.selectOne(wrapper) != null;
    }

    @Override
    public String findPhoneByUid(int uid) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(User::getUphone).eq(User::getUid, uid);
        return this.userMapper.selectOne(wrapper).getUphone();
    }

    @Override
    public User findUserByPhone(String phone) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUphone, phone);
        return this.userMapper.selectOne(wrapper);
    }

    @Override
    public User findUserByUid(int uid) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUid, uid);
        return this.userMapper.selectOne(wrapper);
    }

    @Override
    public void saveUser(String phone) {
        String name = getStringRandom();
        User user = new User();
        user.setUphone(phone);
        user.setUname(name);
        this.userMapper.insert(user);
    }

    @Override
    public void updateUserImage(int id, String url) {
        User user = this.userMapper.selectById(id);
        user.setUheadPath(url);
        this.userMapper.updateById(user);
    }

    @Override
    public User loginByPP(String phone, String password) {
        // 1.将传来的pwd解码
        byte[] b = Base64.getDecoder().decode(password.getBytes());
        String upassword = new String(b);
        // 2.将密码二次加密
        String firstEnc = DigestUtils.md5Hex(upassword);
        String secondEnc = DigestUtils.md5Hex(firstEnc + SALT);
        // 3. 构造查询
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUphone,phone).eq(User::getUpassword,secondEnc);
        // 执行查询
        return this.userMapper.selectOne(wrapper);
    }

    @Override
    public User updateUser(User user) {
        this.userMapper.updateById(user);
        LambdaQueryWrapper<User> wrapper2 = new LambdaQueryWrapper<>();
        wrapper2.eq(User::getUid, user.getUid());
        user = this.userMapper.selectOne(wrapper2);
        return user;
    }

    @Override
    public User updatePwd(User user, String upassword) {
        if (upassword != null) {
            // 解码
            byte[] b = Base64.getDecoder().decode(upassword.getBytes());
            String password = new String(b);
            String firstEncrypt = DigestUtils.md5Hex(password);
            String secondEncrypt = DigestUtils.md5Hex(firstEncrypt + SALT);
            user.setUpassword(secondEncrypt);
            this.userMapper.updateById(user);
        }
        return user;
    }

    @Override
    public void deleteUser(User user) {
        this.userMapper.deleteById(user.getUid());
    }

    /**
     * 随机生成姓名
     */
    public static String getStringRandom() {
        int length = 12;
        String val = "";
        Random random = new Random();

        //参数length，表示生成几位随机数
        for (int i = 0; i < length; i++) {

            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            //输出字母还是数字
            if ("char".equalsIgnoreCase(charOrNum)) {
                //输出是大写字母还是小写字母
                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
                val += (char) (random.nextInt(26) + temp);
            } else if ("num".equalsIgnoreCase(charOrNum)) {
                val += String.valueOf(random.nextInt(10));
            }
        }
        return val;
    }
}
