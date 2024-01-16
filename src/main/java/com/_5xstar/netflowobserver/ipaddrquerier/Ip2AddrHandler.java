package com._5xstar.netflowobserver.ipaddrquerier;


/**
 * 一个网址的查询Ip属地，填充有关对象
 * 庞海文 2024-1-4
**/
public interface Ip2AddrHandler
{
    /**
     * 设置查询头
     * @param urlHead
     */
    void setUrlHead(final String urlHead);

    /**
     * 查询返回数据接口
     * @param strIp
     * @return
     * @throws Exception
     */
    Ip2AddrData handle(final String strIp) throws Exception;



}


