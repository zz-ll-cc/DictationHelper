package com.dictation.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @ClassName Time
 * @Description 需要aop记录当前方法运行时间的时候加这个注解
 * @Author zlc
 * @Date 2020-05-27 16:02
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Time {
}
