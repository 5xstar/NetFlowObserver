package com._5xstar.netflowobserver.ipaddrquerier;

import java.lang.annotation.*;

/**
 * @Ip2AddrHandlerMeta 注解塘
 * 庞海文  2024-1-5
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Ip2AddrHandlerMetas {
    Ip2AddrHandlerMeta[] value();
}
