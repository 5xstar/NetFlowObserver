package com._5xstar.netflowobserver.ipaddrquerier;

import com._5xstar.netflowobserver.ipaddrquerier.handlers.Ip2AddrBaiduHandler;
//import com._5xstar.lljk.handlers.Ip2AddrVoreHandler;

import java.util.ArrayList;

/**
 * 提供查询Ip属地，填充有关对象
 * 庞海文 2024-1-4
 * @Ip2AddrHandlerMeta(urlHead = "https://api.vore.top/api/IPdata?ip=", handlerClass = Ip2AddrVoreHandler.class)  不可用
**/
@Ip2AddrHandlerMeta(urlHead = "https://qifu-api.baidubce.com/ip/geo/v1/district?ip=", handlerClass = Ip2AddrBaiduHandler.class)
public class Ip2AddrService
{

    //查询工具列表
    final private static ArrayList<Ip2AddrHandler> handlers = new ArrayList<>();
    static{
        Class<Ip2AddrService> clazz = Ip2AddrService.class;
        Ip2AddrHandlerMeta[]  metas =clazz.getAnnotationsByType(Ip2AddrHandlerMeta.class);
        Ip2AddrHandler handler;
        for(Ip2AddrHandlerMeta meta : metas){
            try {
                handler = (Ip2AddrHandler)meta.handlerClass().getDeclaredConstructor().newInstance();
                handler.setUrlHead(meta.urlHead());
                handlers.add(handler);
            }catch (Exception e){
               e.printStackTrace();
            }
        }
    }

    /**
     * 解析ip成地址字符串
     * @param strIp
     * @return
     */
    public static String getIp2AddrString(final String strIp){
        final Ip2AddrData dt = getIp2AddrData(  strIp);
        if(dt==null)return null;
        return dt.getDesc();
    }

    /**
     * 获取ip地址数据
     * @param strIp
     * @return
     * @throws Exception
     */
    public static Ip2AddrData getIp2AddrData(final String strIp){
        int length = handlers.size();
        if(length==0)return null;
        final ArrayList<Ip2AddrData> datas = new ArrayList<>();
        for(Ip2AddrHandler handler : handlers){
            try{
                datas.add(handler.handle(strIp));
            }catch (Exception e){
                e.printStackTrace();  //测试
            }
        }
        length = datas.size();
        if(length==0)return null;
        Ip2AddrData data = datas.get(0);
        if(length>1){
            int level1 = getLevel(data);
            int level2;
            for(int i=1; i<length;i++){
                level2 = getLevel(datas.get(i));
                if(level2>level1){
                    data = datas.get(i);
                    level1 = level2;
                }
            }
        }
        return data;
    }

    private static int getLevel(final Ip2AddrData data){
        String s;
        if((s=data.getAddress())!=null && !"".equals(s) )return 5;
        if((s=data.getTown())!=null && !"".equals(s) )return 4;
        if((s=data.getCity())!=null && !"".equals(s) )return 3;
        if((s=data.getProvince())!=null && !"".equals(s) )return 2;
        if((s=data.getCountry())!=null && !"".equals(s) )return 1;
        return 0;
    }


}


