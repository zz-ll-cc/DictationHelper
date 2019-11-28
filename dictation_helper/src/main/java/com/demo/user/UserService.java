package com.demo.user;

import com.demo.common.model.TblUser;
import com.jfinal.plugin.activerecord.Record;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.List;

public class UserService {

    static final String SALT = "haohaoxuexi";


    private TblUser dao = new TblUser().dao();

    /**
     * 修改头像
     * @param id
     * @param url
     */
    public void updateUserImage(int id,String url){
        dao.findById(id).set("uheadPath",url).update();
    }

    /**
     * 用户名密码注册
     *
     * @param name
     * @param password
     */
    public TblUser registByPassword(String name,String phone,String password){
        /**
         * 判断是否存在了相同的手机号
         */
        List<TblUser> userList = dao.find("select * from tbl_user where uphone=? ",phone);
        String firstEncrypt = DigestUtils.md5Hex(password);
        String secondEncrypt = DigestUtils.md5Hex(firstEncrypt+SALT);
        if(userList.isEmpty()){
            new TblUser().set("uname",name).set("uphone",phone).set("upassword",secondEncrypt).save();
            userList = dao.find("select * from tbl_user where uphone=? ",phone);
            return userList.get(0);
        }else{
            return null;
        }
    }


    /**
     * 根据手机号和密码登录
     * @param phone
     * @param password
     * @return
     */
    public TblUser loginByPP(String phone,String password){
        String firstEncrypt = DigestUtils.md5Hex(password);
        String secondEncrypt = DigestUtils.md5Hex(firstEncrypt+SALT);
        List<TblUser> userList = dao.find("select * from tbl_user where uphone = ? and upassword = ? ",phone,secondEncrypt);
        if(userList.isEmpty()){

        }else{
            if(userList.size() == 1){
                return userList.get(0);
            }
        }
        return null;
    }


}
