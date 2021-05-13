package com.ict.annotation;

import java.lang.annotation.*;

/**
 * @Description:
 * @Author wangsr
 * @Date 2021/5/10
 * @Version 1.0
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface BusiLogAnno {
    String oprName() default "";
    String busiName() default "";
    String busiOp() default "";
    Class remarkClass() default com.ict.entity.model.BusiLog.class;
}
