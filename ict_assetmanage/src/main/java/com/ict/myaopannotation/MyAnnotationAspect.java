package com.ict.myaopannotation;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

/**
 * @Description:
 * @Author wangsr
 * @Date 2021/4/28
 * @Version 1.0
 */
@Component
@Aspect
public class MyAnnotationAspect {
    @Pointcut("@annotation(com.ict.myaopannotation.Myannotation)")
    public void myAnnotation(){}

    /**
     * 定制一个环绕通知
     * @param joinPoint
     */
    @Around("myAnnotation()")
    public void advice(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("Around Begin");
        joinPoint.proceed();//执行到这里开始走进来的方法体（必须声明）
        System.out.println("Around End");
    }

    //当想获得注解里面的属性，可以直接注入改注解
    //方法可以带参数，可以同时设置多个方法用&&
    @Before("myAnnotation()")
    public void record(JoinPoint joinPoint) {
        System.out.println("Before");
    }

    @After("myAnnotation()")
    public void after() {
        System.out.println("After");
    }
}