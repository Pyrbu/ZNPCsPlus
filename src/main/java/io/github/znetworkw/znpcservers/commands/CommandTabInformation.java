package io.github.znetworkw.znpcservers.commands;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CommandTabInformation {

    String name();

    String permission();

}
