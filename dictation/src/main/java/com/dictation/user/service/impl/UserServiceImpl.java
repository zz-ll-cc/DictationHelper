package com.dictation.user.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dictation.book.entity.Unit;
import com.dictation.book.service.UnitService;
import com.dictation.mapper.CreditRecordMapper;
import com.dictation.mapper.UnlockMapper;
import com.dictation.mapper.UserMapper;
import com.dictation.user.entity.CreditRecord;
import com.dictation.user.entity.ReasonEnum;
import com.dictation.user.entity.Unlock;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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
    UnlockMapper unlockMapper;

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    CreditRecordMapper creditRecordMapper;

    @Autowired
    UnitService unitService;


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
            logger.error("findUserByUid:  id : " + uid);
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


    //修改数据库，并修改缓存
    @Override
    public User updateUser(User user) {
        try {
            userMapper.updateById(user);
            redisUtil.set(redisUtil.getUserKey(user.getUid()),new ObjectMapper().writeValueAsString(user));
            redisUtil.expire(redisUtil.getUserKey(user.getUid()),60*60);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("updateUser:保存用户信息出错");
        }


//        this.userMapper.updateById(user);
//        LambdaQueryWrapper<User> wrapper2 = new LambdaQueryWrapper<>();
//        wrapper2.eq(User::getUid, user.getUid());
//        user = this.userMapper.selectOne(wrapper2);
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
        try {
            redisUtil.set(redisUtil.getUserKey(uid),new ObjectMapper().writeValueAsString(user),60*60);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            logger.warn(e.toString());
        }

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
        try {
            String key = redisUtil.createDailyActiveUserKey();
            redisUtil.pfAdd(key,id);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("recordActiveUser:写入缓存失败");
        }
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


    /**
     * 签到
     * @param id
     * @return
     */
    @Override
    public User signIn(int id) {
        //获取当前的签到key
        String key = redisUtil.createUserSignInKey(id,null);
        //获取当前是一年中的第几天
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        long day_of_year = calendar.get(Calendar.DAY_OF_YEAR);
        //存入缓存
        redisUtil.setBit(key, day_of_year, true);
        //修改累积签到和连续签到
        //判断是否是连续签到     前一天是否签到了
        if(redisUtil.getBit(key,day_of_year-1)){
            //前一天签到了，连续签到+1
            return updateUserSignIn(id,true);
        }else{
            //前一天没有签到,连续签到归零
            return updateUserSignIn(id,false);
        }

    }


    /**
     *
     * 根据id，在签到时对连续签到和累计签到进行修改
     * 对用户的积分进行修改
     * 插入积分记录表
     *
     * @param id
     * @param is_continuous
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public User updateUserSignIn(int id, boolean is_continuous) {
        User user = userMapper.selectById(id);
        //累计签到+1
        user.setAccumulateSignIn(user.getAccumulateSignIn()+1);
        //修改最后签到日期
        user.setLastSignInTime(new Date());

        if(is_continuous){
            user.setContinuousSignIn(user.getContinuousSignIn()+1);
        }else{
            //如果不是连续签到
            user.setContinuousSignIn(1);
        }
        user.setUserCredit(user.getUserCredit() + ReasonEnum.QD.getCreditNum());

        creditRecordMapper.insert(new CreditRecord().setIncrement(ReasonEnum.QD.getCreditNum()).setUserId(id).setReason(ReasonEnum.QD.getReason()));

        userMapper.updateById(user);

        //更新缓存
        try {
            String key = redisUtil.getUserKey(id);
            redisUtil.set(key,new ObjectMapper().writeValueAsString(user));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            logger.error("用户签到更新缓存时序列化错误");
        }

        return user;
    }


    /**
     * 这里会计算一下当前的连续签到，修改user的累计签到，保存
     *
     * @param id
     * @param accumulate_increment
     * @return
     */
    @Override
    public User updateUserSignIn(int id,int accumulate_increment) {

        User user = this.findUserByUid(id);
        user.setAccumulateSignIn(user.getAccumulateSignIn() + accumulate_increment);

        int total = 0;
        //从今天开始查找连续签到数
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int nowYear = calendar.get(Calendar.YEAR);
        //循环解决跨年的情况,增加循环条件防止死循环
        for( ; redisUtil.exists(redisUtil.createUserSignInKey(id, String.valueOf(nowYear))) && nowYear > 0 ; nowYear--){
            //判断是否为今年
            int startDay = 0;
            if(nowYear == calendar.get(Calendar.YEAR)){
                //如果是今年的话，从今天开始，并且不需要判断key是否存在
                startDay = calendar.get(Calendar.DAY_OF_YEAR);

                for(int i = startDay ; i > 0 ; i-- ){

                    boolean t = redisUtil.getBit(redisUtil.createUserSignInKey(id, String.valueOf(nowYear)),i);
                    total += t ? 1 : 0;
                    //如果不是今天
                    if(i != startDay && !t){
                        //没有连续签到了，那么修改user的连续签到值，并保存
                        user.setContinuousSignIn(total);
                        this.updateUser(user);
                        return user;
                    }
                    //循环正常结束，外层循环判断是否存在上一年的key
                }

            } else {
                //如果不是，从一年的最后一天开始
                //判断是否为闰年
                startDay = nowYear % 4 == 0 ? 366 : 365;
                for(int i = startDay ; i > 0 ; i-- ){

                    boolean t = redisUtil.getBit(redisUtil.createUserSignInKey(id, String.valueOf(nowYear)),i);
                    total += t ? 1 : 0;
                    //如果有一天没签到，中指
                    if(!t){
                        //没有连续签到了，那么修改user的连续签到值，并保存
                        user.setContinuousSignIn(total);
                        this.updateUser(user);
                        return user;
                    }
                }
            }
        }
        return user;
    }


    /**
     * 无论year是否为空，今年的map一定会返回
     *
     * @param id
     * @param year
     * @return
     */
    @Override
    public Map<String, Map<String, String>> getSignInRecordMap(int id, String... year) {
        String key;
        Map<String, Map<String,String>> recordMap = new HashMap<>();
        //查询今年的签到记录,如果没有key，存入null
        key = redisUtil.createUserSignInKey(id,null);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        if(redisUtil.exists(key)){
            //如果key存在，再做这些
            int day_of_year = calendar.get(Calendar.DAY_OF_YEAR);
            Map<String,String> yearMap = new HashMap<>();
            for(int i = day_of_year ; i > 0 ; i--){
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.DAY_OF_YEAR,i);
                Date date = cal.getTime();
                yearMap.put(simpleDateFormat.format(date), String.valueOf(redisUtil.getBit(key,i)));
            }
            recordMap.put(String.valueOf(calendar.get(Calendar.YEAR)),yearMap);
        }else{
            recordMap.put(String.valueOf(calendar.get(Calendar.YEAR)),null);
        }

        if(year == null) return recordMap;

        //查询其他年份的签到记录

        for(String s : year){

            String sKey = redisUtil.createUserSignInKey(id,s);
            //初始化map
            Map<String,String> yearMap = new HashMap<>();
            //不存在key，插入空数据然后跳过遍历
            if(!redisUtil.exists(key)) {
                recordMap.put(s,null);
                continue;
            }
            //检查s的合法性
            Calendar cal = null;
            try {
                cal = Calendar.getInstance();
                cal.setTime(yearFormat.parse(s));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            //与当前年份不重复
            if(cal.get(Calendar.YEAR) == calendar.get(Calendar.YEAR)) continue;

            //判断是否是闰年,如果是闰年，一年有366天
            int total_day = cal.get(Calendar.YEAR)%4 == 0 ? 366 : 365;

            //循环添加数据
            for(int i = total_day ; i > 0 ; i--){
                Calendar c = Calendar.getInstance();
                c.set(Calendar.DAY_OF_YEAR,i);
                Date date = c.getTime();
                yearMap.put(simpleDateFormat.format(date), String.valueOf(redisUtil.getBit(sKey,i)));
            }

            //添加到大map中
            recordMap.put(String.valueOf(calendar.get(Calendar.YEAR)),yearMap);
        }
        return recordMap;
    }

    /**
     * 更新用户最后一次登录时间
     * @param user
     * @return
     */
    @Override
//    @Async("asyncServiceExecutor")
    public User recordLastLoginTime(User user) {

        try {
            user.setLastLoginTime(new Date());
            userMapper.updateById(user);
            user = userMapper.selectById(user.getUid());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("recordLastLoginTime:更新数据库失败");
            return user;
        }
        String key = null;
        try {
            key = redisUtil.getUserKey(user.getUid());
            redisUtil.set(key,new ObjectMapper().writeValueAsString(user));
            redisUtil.expire(key,60*60);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            logger.error("recordLastLoginTime:写入缓存失败");
        }

        return user;

    }


    /**
     * 目前是单日补签，可以拓展为多日期补签
     *
     * @param id
     * @param formatDate    "yyyy-MM-dd"
     * @return
     */
    @Override
    public boolean reSignIn(int id, String formatDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = simpleDateFormat.parse(formatDate);

        } catch (ParseException e) {
            e.printStackTrace();
            logger.error("remedySignIn:日期格式转换出错,传入日期字符串为：" + formatDate);
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        Calendar now = Calendar.getInstance();
        now.setTime(new Date());
        int compareResult = now.compareTo(calendar);
        if(compareResult == -1 || compareResult == 0) {
            logger.error("reSignIn：不能补签今天，或今天以后，传入的formatDate为：" + formatDate);
            return false;
        }

        String key = redisUtil.createUserSignInKey(id, String.valueOf(calendar.get(Calendar.YEAR)));


        //输出错误日志用的
        boolean a = false;
        User u = null;
        boolean b = false;
        try {
            a = redisUtil.setBit(key,calendar.get(Calendar.DAY_OF_YEAR),true);
            u = this.updateUserSignIn(id,1);
        } catch (Exception e) {
            e.printStackTrace();
            b = redisUtil.setBit(key,calendar.get(Calendar.DAY_OF_YEAR),false);
            logger.error("remedySignIn:失败了 , 缓存签到：" + a + " , 数据库修改：" + u + ", 缓存回滚：" + b);
        }
        return true;
    }

    @Override
    public List<CreditRecord> checkUserCreditRecord(int id) {
        QueryWrapper<CreditRecord> queryWrapper = new QueryWrapper<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        queryWrapper
                .like("create_time",simpleDateFormat.format(new Date()))
                .eq("user_id",id);
        return creditRecordMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean unlockUnit(int id, int unitId) {
        User user = userMapper.selectById(id);
        Unit unit = unitService.findOneById(unitId);
        if(user.getUserCredit() < unit.getCost()){
            //积分不足
            return false;
        }
        QueryWrapper<Unlock> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",id).eq("unit_id",unitId);
        if(unlockMapper.selectOne(queryWrapper) == null){
            //插入解锁记录
            Unlock unlock = new Unlock();
            unlock.setUserId(id).setUnitId(unitId);
            unlockMapper.insert(unlock);
            //修改用户的积分
            user.setUserCredit(user.getUserCredit()-unit.getCost());
            userMapper.updateById(user);
            //插入积分修改记录
            CreditRecord creditRecord = new CreditRecord();
            creditRecord.setIncrement(unit.getCost() * -1).setReason("解锁单元").setUserId(user.getUid());
            creditRecordMapper.insert(creditRecord);

            //更新user缓存
            try {
                List<Unlock> unlocks = user.getUnlockList();
                unlocks.add(unlock);
                user.setUnlockList(unlocks);
                redisUtil.set(redisUtil.getUserKey(id),new ObjectMapper().writeValueAsString(user),60*60);
            } catch (JsonProcessingException e) {
                logger.warn("userService中的unlockUnit方法更新redis缓存时json转换出错");
                e.printStackTrace();
            }
        }else{
            //已经存在了解锁数据
            return false;
        }

        return true;
    }




}
