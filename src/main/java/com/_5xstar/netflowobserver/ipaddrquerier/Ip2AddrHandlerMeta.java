package com._5xstar.netflowobserver.ipaddrquerier;

import java.lang.annotation.*;

/**
 * Ip2AddrHandler 元数据
 * 庞海文  2024-1-5
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Repeatable(Ip2AddrHandlerMetas.class)
public @interface Ip2AddrHandlerMeta {
    /**
     * url头
     * @return
     */
    String urlHead();

    /**
     *处理器的类
     * @return
     */
    Class handlerClass();
}
