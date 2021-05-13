package com.ict.aop;

import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ict.annotation.BusiLogAnno;
import com.ict.annotation.SysLogAnno;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description:
 * @Author wangsr
 * @Date 2021/5/11
 * @Version 1.0
 */
@Aspect
@Component
public class SysLogAspect {
    private static final Logger logger = LoggerFactory.getLogger(SysLogAspect.class);

    @Autowired
    private HttpServletRequest httpServletRequest;

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
//    public * com.ict.service.*.*(..)
    private final String POINT_CUT = "execution(public * com.ict.dao.*.*(..))";






    @AfterReturning(value = "@annotation(sysLogAnno)")
    public void saveUserLog(JoinPoint joinPoit, SysLogAnno sysLogAnno) throws Exception {
        logger.info("操作成功" + httpServletRequest.getAttribute("logId"));

    }

    @AfterThrowing(value = "@annotation(sysLogAnno)")
    public void saveUserLog2(JoinPoint joinPoit, BusiLogAnno sysLogAnno) throws Exception {

        logger.info("操作失败" + httpServletRequest.getAttribute("logId"));
    }












//
//    @Pointcut(POINT_CUT)
//    public void pointCut(){}
//
//    @Before(value = "pointCut()")
//    public void before(JoinPoint joinPoint){
//        logger.info("@Before通知执行");
//
//        logger.info("before通知执行结束");
//    }
//
//
//    /**
//     * 后置返回
//     *      如果第一个参数为JoinPoint，则第二个参数为返回值的信息
//     *      如果第一个参数不为JoinPoint，则第一个参数为returning中对应的参数
//     * returning：限定了只有目标方法返回值与通知方法参数类型匹配时才能执行后置返回通知，否则不执行，
//     *            参数为Object类型将匹配任何目标返回值
//     */
//    @AfterReturning(value = POINT_CUT,returning = "result")
//    public void doAfterReturningAdvice1(JoinPoint joinPoint,Object result){
//        logger.info("第一个后置返回通知的返回值："+result);
//    }
//
//    @AfterReturning(value = POINT_CUT,returning = "result",argNames = "result")
//    public void doAfterReturningAdvice2(String result){
//        logger.info("第二个后置返回通知的返回值："+result);
//    }
//    //第一个后置返回通知的返回值：姓名是大大
//    //第二个后置返回通知的返回值：姓名是大大
//    //第一个后置返回通知的返回值：{name=小小, id=1}
//
//
//    /**
//     * 后置异常通知
//     *  定义一个名字，该名字用于匹配通知实现方法的一个参数名，当目标方法抛出异常返回后，将把目标方法抛出的异常传给通知方法；
//     *  throwing:限定了只有目标方法抛出的异常与通知方法相应参数异常类型时才能执行后置异常通知，否则不执行，
//     *            对于throwing对应的通知方法参数为Throwable类型将匹配任何异常。
//     * @param joinPoint
//     * @param exception
//     */
//    @AfterThrowing(value = POINT_CUT,throwing = "exception")
//    public void doAfterThrowingAdvice(JoinPoint joinPoint,Throwable exception){
//        logger.info(joinPoint.getSignature().getName());
//        if(exception instanceof NullPointerException){
//            logger.info("发生了空指针异常!!!!!");
//        }
//        logger.info("后置异常通知");
//        logger.info(httpServletRequest.getAttribute("logId") + "");
//    }
//
//    @After(value = POINT_CUT)
//    public void doAfterAdvice(JoinPoint joinPoint){
//        logger.info("后置通知执行了!");
//        logger.info(httpServletRequest.getAttribute("logId") + "");
//        System.out.println("@Before：目标方法为：" +
//                joinPoint.getSignature().getDeclaringTypeName() +
//                "." + joinPoint.getSignature().getName());
//    }
//
//    /**
//     * 环绕通知：
//     *   注意:Spring AOP的环绕通知会影响到AfterThrowing通知的运行,不要同时使用
//     *
//     *   环绕通知非常强大，可以决定目标方法是否执行，什么时候执行，执行时是否需要替换方法参数，执行完毕是否需要替换返回值。
//     *   环绕通知第一个参数必须是org.aspectj.lang.ProceedingJoinPoint类型
//     */
//    @Around(value = POINT_CUT)
//    public Object doAroundAdvice(ProceedingJoinPoint proceedingJoinPoint){
//        logger.info("@Around环绕通知："+proceedingJoinPoint.getSignature().toString());
//        Object obj = null;
//        try {
//            obj = proceedingJoinPoint.proceed(); //可以加参数
//            logger.info(obj.toString());
//        } catch (Throwable throwable) {
//            throwable.printStackTrace();
//        }
//        logger.info("@Around环绕通知执行结束");
//        return obj;
//    }

}