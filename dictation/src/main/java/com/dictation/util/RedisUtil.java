package com.dictation.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName RedisUtil
 * @Description
 * @Author zlc
 * @Date 2020-04-21 23:36
 */
@Component
public class RedisUtil {

    @Autowired
    RedisTemplate<String,Object> redisTemplate;



    /**
     * 指定缓存失效时间
     * @param key   键
     * @param time  时间(秒)
     * @return
     */
    public boolean expire(String key, long time){
        try {
            if(time > 0){
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     *  根据key 获取过期时间
     * @param key   键   不能为null
     * @return  时间（秒） 返回0代表永久有效
     */
    public long getExpire(String key){
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }


    /**
     * 判断key是否存在
     * @param key   键
     * @return  存在 true 不存在 false
     */
    public boolean hasKey(String key){
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 删除一个或多个key
     * @param key
     */
    public void del(String... key){
        if(key != null && key.length > 0){
            if(key.length == 1){
                redisTemplate.delete(key[0]);
            }else{
                redisTemplate.delete(CollectionUtils.arrayToList(key));
            }
        }
    }

    /**
     * 缓存获取
     * @param key   键
     * @return  值
     */
    public Object get(String key){
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    /**
     * 缓存set
     * @param key   键
     * @param value 值
     * @return
     */
    public boolean set(String key, Object value){
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * set缓存同时设置时间
     * @param key
     * @param value
     * @param time  时间（秒）time小于等于0将设置无限期
     * @return
     */
    public boolean set(String key, Object value, long time){
        try {
            if(time > 0){
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            }else{
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 递增
     * @param key   键
     * @param increment 增量
     * @return
     */
    public long incr(String key, long increment){
        if(increment < 0){
            throw new RuntimeException("增量要大于0");
        }
        return redisTemplate.opsForValue().increment(key, increment);
    }


    /**
     * 递减
     * @param key   键
     * @param decrement 减少量
     * @return
     */
    public long decr(String key, long decrement){
        if(decrement < 0){
            throw new RuntimeException("减少量要大于0");
        }
        return redisTemplate.opsForValue().increment(key, -decrement);
    }


    //=================================Map=======================================


    /**
     * HashGet
     * @param key   键
     * @param item  项
     * @return
     */
    public Object hget(String key, String item){
        return redisTemplate.opsForHash().get(key, item);
    }


    /**
     * 获取hashkey对应的所有键值
     * @param key   键
     * @return      对应的所有键值
     */
    public Map<Object, Object> hmget(String key){
        return redisTemplate.opsForHash().entries(key);
    }


    /**
     * HashSet
     * @param key   键
     * @param map   对应的多个键值
     * @return
     */
    public boolean hmset(String key, Map<String,Object> map){
        try {
            redisTemplate.opsForHash().putAll(key,map);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * HashSet 并设置时间
     * @param key
     * @param map
     * @param time
     * @return
     */
    public boolean hmset(String key, Map<String, Object> map, long time){
        try {
            redisTemplate.opsForHash().putAll(key,map);
            if(time > 0){
                expire(key,time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 向hash表中放数据，如果不存在将创建
     * @param key   键
     * @param item  项
     * @param value 值
     * @return
     */
    public boolean hset(String key, String item, Object value){
        try {
            redisTemplate.opsForHash().put(key, item, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 向hash表中放数据，如果不存在将创建
     *
     * @param key
     * @param item
     * @param value
     * @param time  将会替换原来hash表的时间
     * @return
     */
    public boolean hset(String key, String item, Object value, long time){
        try {
            redisTemplate.opsForHash().put(key, item, value);
            if(time > 0){
                expire(key,time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除hash表中的值
     * @param key
     * @param item
     */
    public void hdel(String key, Object... item){
        redisTemplate.opsForHash().delete(key,item);
    }


    /**
     * 判断hash表中是否有该项的值
     * @param key
     * @param item
     * @return
     */
    public boolean hHasKey(String key, String item){
        return redisTemplate.opsForHash().hasKey(key, item);
    }


    /**
     * hash递增，如果不存在则会创建
     * @param key
     * @param item
     * @param by
     * @return
     */
    public long hincr(String key, String item, long by){
        return redisTemplate.opsForHash().increment(key, item, by);
    }

    /**
     * hash递减
     * @param key
     * @param item
     * @param by
     * @return
     */
    public long hdecr(String key, String item, long by){
        return redisTemplate.opsForHash().increment(key, item, -by);
    }


    //=========================================Set=================================================


    /**
     * 根据key获取set中的值
     * @param key
     * @return
     */
    public Set<Object> sget(String key){
        try {
            return redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 根据value从set中查询是否存在
     * @param key
     * @param value
     * @return
     */
    public boolean sIsMember(String key, Object value){
        try {
            return redisTemplate.opsForSet().isMember(key, value);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 将数据放入set缓存
     * @param key
     * @param value
     * @return
     */
    public long sAdd(String key, Object... value){
        try {
            return redisTemplate.opsForSet().add(key, value);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }


    /**
     * 将set数据放入缓存
     * @param key
     * @param time  时间（秒）
     * @param value
     * @return
     */
    public long sAddWithTime(String key, long time, Object... value){
        try {
            Long count = redisTemplate.opsForSet().add(key, value);
            if(time > 0){
                expire(key, time);
            }
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }


    /**
     * 获取set的长度
     * @param key
     * @return
     */
    public long sGetSize(String key){
        try {
            return redisTemplate.opsForSet().size(key);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }


    /**
     * 移除值为value的
     * @param key
     * @param values
     * @return
     */
    public long setRemove(String key, Object... values){
        try {
            Long count = redisTemplate.opsForSet().remove(key, values);
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    //==================================List============================================


    /**
     * 获取list缓存中的内容
     * @param key
     * @param start
     * @param end
     * @return
     */
    public List<Object> lGet(String key, long start, long end){
        try {
            return redisTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 获取list缓存的长度
     * @param key
     * @return
     */
    public long lGetListSize(String key){
        try {
            return redisTemplate.opsForList().size(key);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }


    /**
     * 通过索引获取list中的值
     * @param key
     * @param index
     * @return
     */
    public Object lGetIndex(String key, long index){
        try {
            return redisTemplate.opsForList().index(key, index);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 将list放入缓存，从left
     * @param key
     * @param value
     * @return
     */
    public boolean lSet(String key, Object value){
        try {
            redisTemplate.opsForList().leftPush(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将list放入缓存，从right
     * @param key
     * @param value
     * @return
     */
    public boolean rSet(String key, Object value){
        try {
            redisTemplate.opsForList().rightPush(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 将list放入缓存，从left，设置时间
     * @param key
     * @param value
     * @param time
     * @return
     */
    public boolean lSet(String key, Object value, long time){
        try {
            redisTemplate.opsForList().leftPush(key, value);
            if(time > 0){
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 将list放入缓存，从left，设置时间
     * @param key
     * @param value
     * @param time
     * @return
     */
    public boolean rSet(String key, Object value, long time){
        try {
            redisTemplate.opsForList().rightPush(key, value);
            if(time > 0){
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将list放入缓存，left
     * @param key
     * @param value
     * @return
     */
    public boolean lSet(String key, List<Object> value){
        try {
            redisTemplate.opsForList().leftPushAll(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将list放入缓存，left，设置时间
     * @param key
     * @param value
     * @param time
     * @return
     */
    public boolean lSet(String key, List<Object> value, long time){
        try {
            redisTemplate.opsForList().leftPushAll(key, value);
            if(time > 0){
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 将list放入缓存，right
     * @param key
     * @param value
     * @return
     */
    public boolean rSet(String key, List<Object> value){
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将list放入缓存，right，设置时间
     * @param key
     * @param value
     * @param time
     * @return
     */
    public boolean rSet(String key, List<Object> value, long time){
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            if(time > 0){
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 根据索引修改list中某条数据
     * @param key
     * @param index
     * @param value
     * @return
     */
    public boolean lUpdateIndex(String key, long index, Object value){
        try {
            redisTemplate.opsForList().set(key, index, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 移除N个值为value
     * @param key
     * @param count
     * @param value
     * @return
     */
    public long lRemove(String key, long count, Object value){
        try {
            Long remove = redisTemplate.opsForList().remove(key, count, value);
            return remove;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }


}
