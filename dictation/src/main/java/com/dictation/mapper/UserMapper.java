package com.dictation.mapper;

import com.dictation.user.entity.User;
import org.apache.ibatis.annotations.Param;

/**
 * @ClassName: UserMapper
 * @Description: TODO
 * @Author: szy
 * @Date 2020/4/14
 */
public interface UserMapper {
    // 查看传入的phone是否已经存在
    public User checkPhone(String phone);
    // 根据传进来的phone查找对应user对象
    public User findUserByPhone(String phone);
    // 根据uid查询phone
    public String findPhoneByUid(int uid);
    // 根据传入的 uid 获取 User 对象
    public User findUserByUid(int uid);
    // 插入保存 user
    public void  saveUser(User user);
    // 修改头像，根据传入的 uid ，修改对应图像位置 === 传入数据库
    public void updateUserImage(@Param("uid") int id, @Param("url") String url);
    // 根据手机号和密码
    public User loginByPP(@Param("phone") String phone, @Param("password") String password);
    // 修改用户信息
    public void updateUser(User user);
    // 修改密码
    public void updatePwd(@Param("uid") int uid, @Param("password") String password);
    // 删除用户
    public void deleteUser(int uid);
}
