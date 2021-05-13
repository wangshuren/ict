package com.ict.myaopannotation;

import java.lang.annotation.*;

/**
 * @Description:
 * @Author wangsr
 * @Date 2021/4/28
 * @Version 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MyAnnotation {
}
