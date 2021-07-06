package com.ict.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
public @interface QueueConsumer {

    String queue();

    int currentConsumer() default 1;
}
