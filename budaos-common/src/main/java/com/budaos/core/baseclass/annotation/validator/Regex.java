package com.budaos.core.baseclass.annotation.validator;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Documented
public @interface Regex {
    String value();

    String msg() default "";
}
