package com._5xstar.netflowobserver.counter;

import java.io.Serializable;

/**
 * 监听项目
 * 庞海文  2023-12-25
 */
public interface ListenItem extends Serializable {
     String getIp();
     void setAddr(final String addr);
     String getAddr();
}
