package com.ict.myaopannotation;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Author wangsr
 * @Date 2021/4/28
 * @Version 1.0
 */
@RestController
@RequestMapping("/annotation")
public class MyAnnotationTestController {

    @RequestMapping("/test")
    public void testAnnotation() {
        System.out.println(3);
    }
}