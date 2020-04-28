package com.dictation.user.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dictation.mapper.CreditRecordMapper;
import com.dictation.mapper.UserMapper;
import com.dictation.user.entity.CreditRecord;
import com.dictation.user.entity.User;
import com.dictation.user.service.UserService;
import com.dictation.util.RedisUtil;
import com.dictation.util.TimeUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;
import java.util.Random;

/**
 * @ClassName: UserServiceImpl
 * @Description: TODO
 * @Author: szy/zlc
 * @Date 2020/4/14
 */
@Service("userServiceImpl")
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    CreditRecordMapper creditRecordMapper;

    private Logger logger = LoggerFactory.getLogger(this.getClass());


    static final String SALT = "haohaoxuexi";

    @Override
    public boolean checkUser(String phone) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
//        wrapper.select(User::getUid).eq(User::getUphone, phone);
        wrapper.eq(User::getUphone, phone);
        return this.userMapper.selectOne(wrapper) != null;
    }

    @Override
    public String findPhoneByUid(int uid) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
//        wrapper.select(User::getUphone).eq(User::getUid, uid);
        wrapper.eq(User::getUid, uid);
        return this.userMapper.selectOne(wrapper).getUphone();
    }


    /**
     * 根据手机号查询user，如果当前user不在缓存中，把user存入缓存
     * @param phone
     * @return
     */
    @Override
    public User findUserByPhone(String phone) {
        User user = null;
        String uStr;
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUphone, phone);
        if((user = userMapper.selectOne(wrapper)) == null){
            return null;
        }
        String key = redisUtil.getUserKey(user.getUid());
        try {
            if((uStr = (String) redisUtil.get(key)) == null){
                uStr = new ObjectMapper().writeValueAsString(user);
                redisUtil.set(key,uStr,60*60);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return user;
    }


    /**
     * 用户信息存入缓存
     * @param uid
     * @return
     */
    @Override
    public User findUserByUid(int uid) {
        String uStr;
        User user = null;
        String key = redisUtil.getUserKey(uid);
        try {
            if((uStr = (String) redisUtil.get(key)) == null){
                LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(User::getUid, uid);
                user = userMapper.selectOne(wrapper);
                uStr = new ObjectMapper().writeValueAsString(user);
                redisUtil.set(key,uStr,60*60);
            }else{
                redisUtil.expire(key,60*60);
                user = new ObjectMapper().readValue(uStr,User.class);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return user;
    }


    /**
     * 保存用户信息的同时存入缓存
     * @param phone
     */
    @Override
    public void saveUser(String phone) {
        String name = getStringRandom();
        User user = new User();
        user.setUphone(phone);
        user.setUname(name);
        userMapper.insert(user);
        String key = redisUtil.getUserKey(user.getUid());
        try {
            redisUtil.set(key,new ObjectMapper().writeValueAsString(user),60*60);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }


    /**
     * 如果不在缓存中，不作处理
     * 如果在缓存中，更新缓存
     * @param id
     * @param url
     */
    @Override
    public void updateUserImage(int id, String url) {
        String key = redisUtil.getUserKey(id);
        String uStr;
        User user = this.userMapper.selectById(id);
        user.setUheadPath(url);
        try {
            if((uStr = (String) redisUtil.get(key)) != null){
                uStr = new ObjectMapper().writeValueAsString(user);
                redisUtil.set(key,uStr,60*60);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
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


    @Override
    @Async("asyncServiceExecutor")
    @Transactional(rollbackFor = Exception.class)
    public void updateUserCreditAndInsertRecordAsync(int uid, String changReason, int changeNum) {
        User user = userMapper.selectById(uid);
        user.setUserCredit(user.getUserCredit() + changeNum);
        userMapper.updateById(user);
        creditRecordMapper.insert(new CreditRecord().setIncrement(changeNum).setUserId(uid).setReason(changReason));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public User updateUserCreditAndInsertRecord(int uid, String changReason, int changeNum) {
        User user = userMapper.selectById(uid);
        user.setUserCredit(user.getUserCredit() + changeNum);
        userMapper.updateById(user);
        creditRecordMapper.insert(new CreditRecord().setIncrement(changeNum).setUserId(uid).setReason(changReason));
        return user;
    }


    /**
     * 每天凌晨4点执行
     * 设置14天过期
     */
    @Scheduled(cron = "0 0 4 * * ?")
    @Override
    public void persistDailyActiveUser(){
        //如redis里找昨天的hyperloglog并且记录日志文件，然后设置过期时间
        String key = redisUtil.getYesterdayActiveUserKey();
        long result = redisUtil.pfCount(key);
        redisUtil.expire(key,1209600);
        logger.warn("昨日活跃用户数为："+result);
    }


    @Override
    public void recordActiveUser(int id){
        String key = redisUtil.createDailyActiveUserKey();
        redisUtil.pfAdd(key,id);
    }


    /**
     * 用户连续签到天数增加
     * @param id
     * @return
     */
    @Override
    public long continuousSignIn(int id){
        String key = redisUtil.createUserContinusSignInKey(id);
        if(!redisUtil.exists(key)){
            redisUtil.set(key,1,TimeUtil.getSecondsToNextDay12pm());
            return 1;
        }else{
            return redisUtil.incr(key,1,TimeUtil.getSecondsToNextDay12pm());
        }
    }


    /**
     * 获取连续签到天数
     * @param id
     * @return
     */
    @Override
    public long getContinuousSignIn(int id){
        String key = redisUtil.createUserContinusSignInKey(id);
        if(!redisUtil.exists(key)){
            return 0;
        }else{
            return (long) redisUtil.get(key);
        }
    }





}
