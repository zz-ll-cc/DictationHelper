package com.dictation.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @ClassName ExecutionAspect
 * @Description
 * @Author zlc
 * @Date 2020-05-27 15:50
 */

@Aspect
@Component
public class ExecutionAspect {


    private Logger logger = LoggerFactory.getLogger(this.getClass());


    @Pointcut("@annotation(com.dictation.annotations.Time)")
    public void redeemPointcut(){}


    @Around("redeemPointcut()")
    public Object timeCount(ProceedingJoinPoint joinPoint) throws Throwable {

        Long startTime = System.currentTimeMillis();

        Object o = joinPoint.proceed();

        Long endTime = System.currentTimeMillis();

        Long tempTime = (endTime - startTime);

        logger.info("方法全限定类名：" + joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + ",花费时间：" +
                (((tempTime/86400000)>0)?((tempTime/86400000)+"d"):"")+
                ((((tempTime/86400000)>0)||((tempTime%86400000/3600000)>0))?((tempTime%86400000/3600000)+"h"):(""))+
                ((((tempTime/3600000)>0)||((tempTime%3600000/60000)>0))?((tempTime%3600000/60000)+"m"):(""))+
                ((((tempTime/60000)>0)||((tempTime%60000/1000)>0))?((tempTime%60000/1000)+"s"):(""))+
                ((tempTime%1000)+"ms"));

        return o;
    }




}
