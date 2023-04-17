package io.github.znetworkw.znpcservers.commands;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CommandInformation {
    String[] arguments();

    String[] help() default {};

    String name();

    String permission();

    boolean isMultiple() default false;
}
