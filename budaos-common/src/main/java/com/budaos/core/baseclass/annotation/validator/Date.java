package com.budaos.core.baseclass.annotation.validator;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Documented
public @interface Date {
    String format();

    String msg() default "";
}
