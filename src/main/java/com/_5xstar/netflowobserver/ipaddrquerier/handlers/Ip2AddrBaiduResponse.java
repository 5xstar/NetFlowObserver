package com._5xstar.netflowobserver.ipaddrquerier.handlers;

import com._5xstar.netflowobserver.ipaddrquerier.Ip2AddrData;
import com._5xstar.netflowobserver.ipaddrquerier.Ip2AddrJsonResponse;
import com.alibaba.fastjson2.annotation.JSONField;

/**
 * 百度api查询返回处理
 * 庞海文  2024-1-5
 *
 * 设置ip地址及网络供应商
 * https://qifu-api.baidubce.com/ip/geo/v1/district?ip=14.210.192.141
 *
 {"code":"Success","data":
 {"continent":"亚洲","country":"中国","zipcode":"524033","timezone":"UTC+8","accuracy":"区县","owner":"中国电信","isp":"中国电信",
 "source":"数据挖掘","areacode":"CN","adcode":"440802","asnumber":"4134","lat":"21.283979","lng":"110.368704","radius":"9.9437",
 "prov":"广东省","city":"***","district":"***"},
 "charge":true,"msg":"查询成功","ip":"14.210.192.141","coordsys":"WGS84"}
 *
 */
public  class Ip2AddrBaiduResponse implements Ip2AddrJsonResponse {
    @JSONField
    public String code;
    @JSONField
    public BaiduData data;
    @Override
    public Ip2AddrData getData() {
        return this.data;
    }

    private static class BaiduData implements Ip2AddrData{
        @JSONField
        public String country;
        @JSONField
        public String prov;
        @JSONField
        public String city;
        @JSONField
        public String district;
        @JSONField
        public String isp;

        final private long time = System.currentTimeMillis();
        @Override
        public long getTime(){
            return this.time;
        }

        @Override
        public String getCountry() {
            return this.country;
        }

        @Override
        public String getProvince() {
            return this.prov;
        }

        @Override
        public String getCity() {
            return this.city;
        }

        @Override
        public String getTown() {
            return this.district;
        }

        @Override
        public String getNet() {
            return this.isp;
        }
    }


}
