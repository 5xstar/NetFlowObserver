package com._5xstar.netflowobserver.ipaddrquerier.handlers;

import com._5xstar.netflowobserver.ipaddrquerier.Ip2AddrHttpsJsonHandler;

public class Ip2AddrBaiduHandler extends Ip2AddrHttpsJsonHandler<Ip2AddrBaiduResponse> {

    /**
     * 获取response类
     * @return
     */
    @Override
    protected Class<Ip2AddrBaiduResponse> getResponseClass() {
        return Ip2AddrBaiduResponse.class;
    }




}
