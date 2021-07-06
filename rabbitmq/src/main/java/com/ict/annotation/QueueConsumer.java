/**
 * Copyright (C) 2006-2019 Wulian All rights reserved
 */
package com.ict.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
public @interface QueueConsumer {

    String queue();

    int currentConsumer() default 1;
}
