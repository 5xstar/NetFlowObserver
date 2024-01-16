package com._5xstar.netflowobserver.ipaddrquerier;

import com.alibaba.fastjson2.JSON;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * api查询
 * 庞海文  2024-1-5
 */
public  abstract class Ip2AddrHttpsJsonHandler<R extends Ip2AddrJsonResponse>  implements Ip2AddrHandler{


    /**
     * 设置查询头
     * @param urlHead
     */
    protected String urlHead;
    @Override
    public void setUrlHead(final String urlHead){
        this.urlHead=urlHead;
    }

    /**
     * 获取response类
     * @return
     */
    protected abstract Class<R> getResponseClass();

    /**
     * 查询返回数据接口
     * @param strIp
     * @return
     * @throws Exception
     */
    @Override
    public Ip2AddrData handle(final String strIp) throws Exception{
        //从缓存中取
        Ip2AddrData data = doAddr(strIp, null);
        if(data!=null)return data;
        final String json = Ip2AddrUtil.httpsGet(urlHead + strIp);
        //System.out.println("json=" + json);
        if (json != null) {
            R resp = JSON.parseObject(json, getResponseClass());
            if (resp != null && (data=resp.getData())!=null) {
                doAddr(strIp, data);  //缓存ipaddr
                return data;
            }
        }
        throw new Exception(getResponseClass().getName()+"处理器无法获取！");
    }

    private static class CacheAddr implements Serializable {
        final private long time;
        final private Ip2AddrData data;
        private CacheAddr(final  long time, final  Ip2AddrData data){
            this.time=time;
            this.data=data;
        }
    }

    //每一个处理器有一个缓存器
    final private HashMap<String, CacheAddr> cacheAddrs =new HashMap<>(Const.maxCacheNum);
    private synchronized  Ip2AddrData doAddr(final String ip, final Ip2AddrData data ){
        if(data!=null){
            final long time = System.currentTimeMillis();
            if(cacheAddrs.size()> Const.maxCacheNum-10){
                String k;
                CacheAddr v;
                for(Map.Entry<String,CacheAddr>  kca : cacheAddrs.entrySet()){   //清理半天前的记录
                    v = kca.getValue();
                    if(v.time + Const.maxCacheTimeMillis/2 < time)cacheAddrs.remove(kca.getKey());
                }
            }
            cacheAddrs.put(ip, new CacheAddr(time, data));
        }else{
            CacheAddr cacheAddr = cacheAddrs.get(ip);
            if(cacheAddr!=null){
                if(cacheAddr.time+ Const.maxCacheTimeMillis<System.currentTimeMillis()){  //放弃1天前的记录
                    cacheAddrs.remove(ip);
                }else return cacheAddr.data;
            }
        }
        return null;
    }
}
