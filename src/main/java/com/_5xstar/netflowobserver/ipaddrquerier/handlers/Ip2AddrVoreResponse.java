package com._5xstar.netflowobserver.ipaddrquerier.handlers;

import com._5xstar.netflowobserver.ipaddrquerier.Ip2AddrData;
import com._5xstar.netflowobserver.ipaddrquerier.Ip2AddrJsonResponse;
import com.alibaba.fastjson2.annotation.JSONField;

/**
 *api.vore.top查询返回处理
 * 庞海文  2024-1-6
 *
 *  https://api.vore.top/api/IPdata?ip=14.210.*.*
 * {
 *     "code": 200,
 *     "msg": "SUCCESS",
 *     "ipinfo": {
 *         "type": "ipv4",
 *         "text": "14.210.*.*",
 *         "cnip": true
 *     },
 *     "ipdata": {
 *         "info1": "广东省",
 *         "info2": "**市",
 *         "info3": "**",
 *         "isp": "电信"
 *     },
 *     "adcode": {
 *         "o": "广东省*** - 电信",
 *         "p": "广东",
 *         "c": "**",
 *         "n": "广东-**",
 *         "r": "广东-**",
 *         "a": "440000",
 *         "i": true
 *     },
 *     "tips": "接口由VORE-API(https:\/\/api.vore.top\/)免费提供",
 *     "time": 1703951603
 * }
 */
public  class Ip2AddrVoreResponse implements Ip2AddrJsonResponse, Ip2AddrData {
    @JSONField
    public String code;
    @JSONField
    public String msg;
    @JSONField
    public VoreIpinfo ipinfo;
    @JSONField
    public VoreIpdata ipdata;
    @Override
    public Ip2AddrData getData() {
        return this;
    }

    final private long time = System.currentTimeMillis();
    @Override
    public long getTime(){
        return this.time;
    }

    @Override
    public String getCountry() {
        if(this.ipinfo!=null && this.ipinfo.cnip)return "中国";
        else if(this.ipdata==null)return null;
        return this.ipdata.info1;
    }

    @Override
    public String getProvince() {
        if(this.ipdata==null)return null;
        return this.ipinfo!=null && this.ipinfo.cnip?this.ipdata.info1:this.ipdata.info2;
    }

    @Override
    public String getCity() {
        if(this.ipdata==null)return null;
        return this.ipinfo!=null && this.ipinfo.cnip?this.ipdata.info2:this.ipdata.info3;
    }

    @Override
    public String getTown() {
        if(this.ipdata==null)return null;
        return this.ipinfo!=null && this.ipinfo.cnip?this.ipdata.info3:null;
    }

    @Override
    public String getNet() {
        if(this.ipdata==null)return null;
        return this.ipdata.isp;
    }
    private static class VoreIpinfo{
        @JSONField
        public boolean cnip;
    }
    private static class VoreIpdata{
        @JSONField
        public String info1;
        @JSONField
        public String info2;
        @JSONField
        public String info3;
        @JSONField
        public String isp;
    }
}
