package com._5xstar.netflowobserver.counter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 文章监控常数
 * 庞海文  2023-12-25
 */
public class Const {
    //本应用用的执行池,在定时执行器中关闭
    public final static ExecutorService es= Executors.newCachedThreadPool();
    // 长时间格式
    public static SimpleDateFormat SIP_TIMESTAMP_FORMATER =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    //获取长时间格式字符串
    public  static String getTime(Date dTime) {
        return SIP_TIMESTAMP_FORMATER.format(dTime);
    }

    //最多放2000条消息
    public static int maxMsgLength=2000;

    //每页的大小
    public static int pageSize = 20;

    //获取ip地址的url头
    public static String addrUrlHeadBaidu = "https://qifu-api.baidubce.com/ip/geo/v1/district?ip=";
    public static String addrUrlHeadVore = "https://api.vore.top/api/IPdata?ip=";

    //ip地址缓存时间1天
    public static long maxCacheTimeMillis = 24L*60*60*1000;



}
