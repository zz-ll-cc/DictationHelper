package com.dictation.user.entity;

/**
 * @ClassName: LoginInfo
 * @Description: TODO 登陆时返回信息
 * @Author: szy
 * @Date 2020/4/15
 */
public class LoginInfo {
    /**
     * switch(regist_type)
     *          验证码             账号密码            修改密码
     * case 1:  未注册             成功                  成功
     * case 2:  已注册             账号未注册          旧密码错误
     * case 3:                    密码错误
     */
    private int register_type;
    private User user;

    public int getRegister_type() {
        return register_type;
    }

    public void setRegister_type(int register_type) {
        this.register_type = register_type;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
