package com.dictation.user.service;

import com.dictation.user.entity.CreditRecord;
import com.dictation.user.entity.User;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: UserService
 * @Description: TODO
 * @Author: szy
 * @Date 2020/4/14
 */
public interface UserService {
    // 通过手机号检测是否存在User对象
    boolean checkUser(String phone);

    // 根据uid查询手机信息
    String findPhoneByUid(int uid);

    // 根据手机号查找对应User信息
    User findUserByPhone(String phone);

    // 根据Uid查找对应User信息
    User findUserByUid(int uid);

    // 新建 User 对象，随机生成 userName
    void saveUser(String phone);

    // 根据uid更新头像 ====> 数据库
    void updateUserImage(int id,String url);

    // 根据手机号和密码
    User loginByPP(String phone, String password);

    // 上传头像
    //public void uploadHead(User user);

    // 修改用户信息
    User updateUser(User user);

    // 修改密码
    User updatePwd(User user,String upassword);

    // 删除用户
    void deleteUser(User user);

    //增加用户积分并添加积分记录
    User updateUserCreditAndInsertRecord(int uid, String changReason, int changeNum);

    void updateUserCreditAndInsertRecordAsync(int uid, String changReason, int changeNum);

    void persistDailyActiveUser();

    void recordActiveUser(int id);


    long continuousSignIn(int id);


    long getContinuousSignIn(int id);

    //签到
    User signIn(int id);


    //签到时 修改连续签到和累计签到
    User updateUserSignIn(int id,boolean is_continuous);

    //补签时 根据数据修改  hint为提示
    User updateUserSignIn(int id,int accumulate_increment);

    //去缓存中查找用户签到map
    Map<String, Map<String,String>> getSignInRecordMap(int id, String... year);

    //记录上一次登录日期时间
    User recordLastLoginTime(User user);

    //补签
    boolean reSignIn(int id,String formatDate);

    //获取用户积分记录
    List<CreditRecord> checkUserCreditRecord(int id);




}
