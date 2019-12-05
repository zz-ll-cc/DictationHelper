package com.demo.user;

import com.demo.common.model.TblUser;
import com.demo.utils.RandomUtil;
import com.jfinal.plugin.activerecord.Record;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.List;

public class UserService {

    static final String SALT = "haohaoxuexi";


    private TblUser dao = new TblUser().dao();
    /**
     * 判断手机号是否存在
     * @param phone 手机号
     * @return
     */
    public boolean  checkPhone(String phone){
        List<TblUser> userList = dao.find("select * from tbl_user where uphone=? ", phone);
        if (userList.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 通过手机号查找用户
     * @param phone
     * @return
     */
    public TblUser findUserByPhone(String phone){
        List<TblUser> userList = dao.find("select * from tbl_user where uphone=? ", phone);
        if (userList.isEmpty()) {
            return null;
        } else {
            return userList.get(0);
        }
    }

    /**
     *通过Uid查找用户
     * @param uid
     * @return
     */
    public TblUser findUserByUid(int uid){
        List<TblUser> userList = dao.find("select * from tbl_user where uid=? ", uid);
        if (userList.isEmpty()) {
            return null;
        } else {
            return userList.get(0);
        }
    }
    /**
     * 插入User
     * @param phone
     */
    public void  saveUser(String phone){
        String name= RandomUtil.getStringRandom();
        new TblUser().set("uphone", phone).set("uname",name).save();
    }
    /**
     * 修改头像
     *
     * @param id
     * @param url
     */
    public void updateUserImage(int id, String url) {
         dao.findById(id).set("uheadPath", url).update();
    }
    /**
     * 用户名密码注册
     *
     * @param name
     * @param password
     */
    public TblUser registByPassword(String name, String phone, String password) {
        /**
         * 判断是否存在了相同的手机号
         */
        List<TblUser> userList = dao.find("select * from tbl_user where uphone=? ", phone);
        String firstEncrypt = DigestUtils.md5Hex(password);
        String secondEncrypt = DigestUtils.md5Hex(firstEncrypt + SALT);
        if (userList.isEmpty()) {
            new TblUser().set("uname", name).set("uphone", phone).set("upassword", secondEncrypt).save();
            userList = dao.find("select * from tbl_user where uphone=? ", phone);
            return userList.get(0);
        } else {
            return null;
        }
    }


    /**
     * 根据手机号和密码登录
     *
     * @param phone
     * @param password
     * @return
     */
    public TblUser loginByPP(String phone, String password) {
        String firstEncrypt = DigestUtils.md5Hex(password);
        String secondEncrypt = DigestUtils.md5Hex(firstEncrypt + SALT);
        List<TblUser> userList = dao.find("select * from tbl_user where uphone = ? and upassword = ? ", phone, secondEncrypt);
        if (userList.isEmpty()) {

        } else {
            if (userList.size() == 1) {
                return userList.get(0);
            }
        }
        return null;
    }

    public TblUser updateUser(TblUser user){
        String password=user.getUpassword();
        String firstEncrypt = DigestUtils.md5Hex(password);
        String secondEncrypt = DigestUtils.md5Hex(firstEncrypt + SALT);
        user.set("upassword",secondEncrypt);
        user.update();
        return user;
    }


    public void deleteUser(TblUser user){
        user.delete();
    }


}
