package com._5xstar.netflowobserver.counter;

import com.alibaba.fastjson2.annotation.JSONField;

public class Visitor implements ListenItem {
    @JSONField(name="TM")
    public long time;
    @JSONField(name="N")
    public int visitNum;
    @JSONField(name="U")
    public String url;
    @JSONField(name="IP")
    public String remoteIp;
    @Override
    public String getIp(){return remoteIp;}
    @JSONField(name="AD")
    public String addr="未知";
    @Override
    public void setAddr(final String addr){
        this.addr=addr;
    }
    @Override
    public String getAddr(){
        if(this.addr.equals("未知"))return null;
        return this.addr;
    }
    @JSONField(name="IT")
    public String itemId;
    @JSONField(name="TT")
    public String title;
    @JSONField(name="MI")
    public String mainImg;
    @JSONField(name="UA")
    public String user_agent;

}
