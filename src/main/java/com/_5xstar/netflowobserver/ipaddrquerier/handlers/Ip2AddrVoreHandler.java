package com._5xstar.netflowobserver.ipaddrquerier.handlers;

import com._5xstar.netflowobserver.ipaddrquerier.Ip2AddrHttpsJsonHandler;

public class Ip2AddrVoreHandler extends Ip2AddrHttpsJsonHandler<Ip2AddrVoreResponse> {

    /**
     * 获取response类
     * @return
     */
    @Override
    protected Class<Ip2AddrVoreResponse> getResponseClass() {
        return Ip2AddrVoreResponse.class;
    }


}
