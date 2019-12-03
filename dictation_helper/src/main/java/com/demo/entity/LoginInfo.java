package com.demo.entity;

import com.demo.common.model.TblUser;

public class LoginInfo {
    private int Register_type;
    private TblUser user;

    public int getRegister_type() {
        return Register_type;
    }

    public void setRegister_type(int register_type) {
        Register_type = register_type;
    }

    public TblUser getUser() {
        return user;
    }

    public void setUser(TblUser user) {
        this.user = user;
    }
}
