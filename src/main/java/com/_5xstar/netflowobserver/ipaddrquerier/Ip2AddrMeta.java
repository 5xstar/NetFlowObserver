package com._5xstar.netflowobserver.ipaddrquerier;

import java.lang.annotation.*;

/**
 * 向外查询ip属地的url请求头
 * 庞海文  2023-1-4
 */

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Ip2AddrMeta {
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
