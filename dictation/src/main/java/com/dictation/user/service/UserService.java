package com.dictation.user.service;

import com.dictation.user.entity.User;

/**
 * @ClassName: UserService
 * @Description: TODO
 * @Author: szy
 * @Date 2020/4/14
 */
public interface UserService {
    // 通过手机号检测是否存在User对象
    public boolean checkUser(String phone);

    // 根据uid查询手机信息
    public String findPhoneByUid(int uid);

    // 根据手机号查找对应User信息
    public User findUserByPhone(String phone);

    // 根据Uid查找对应User信息
    public User findUserByUid(int uid);

    // 新建 User 对象，随机生成 userName
    public void saveUser(String phone);

    // 根据uid更新头像 ====> 数据库
    public void updateUserImage(int id,String url);

    // 根据手机号和密码
    public User loginByPP(String phone, String password);

    // 上传头像
    public void uploadHead(User user);

    // 修改用户信息
    public User updateUser(User user);

    // 修改密码
    public User updatePwd(User user,String upassword);

    // 删除用户
    public void deleteUser(User user);
}
