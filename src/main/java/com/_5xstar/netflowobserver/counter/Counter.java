/**
 *网页访问计数
**/
package com._5xstar.netflowobserver.counter;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Counter {

    /*
     *@itemId 网页id 
     *@title 网页标题
     *@mainImg 网页主图 
    **/
    private static int visitNum=0;
    /*public static void count(
      final HttpServletRequest request,
      final HttpServletResponse response,
      final String itemId
    ) throws ServletException, IOException{
        count(request, response,itemId, null, null);
    }

    public static void count(
      final HttpServletRequest request,
      final HttpServletResponse response,
      final String itemId, 
      final String title
    )throws ServletException, IOException{
       count(request, response, itemId, title, null);
    }

    public static void count(
      final HttpServletRequest request,
      final HttpServletResponse response,
      final String itemId,
      final String title,
      final String mainImg
    )throws ServletException, IOException{
        count(request, response, itemId, title, mainImg, null);
    }*/

    private static Matcher botua_matcher = Pattern.compile("bot|spider|yahoo", Pattern.CASE_INSENSITIVE).matcher("");

    public static void count(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final String itemId,
            final String title,
            final String mainImg,
            final String redirect_url
    )throws IOException {
        count(  request,  response,  itemId,  title,  mainImg,  redirect_url, getIPAddress(request));
    }
    public static void count(
        final HttpServletRequest request,
        final HttpServletResponse response,
        final String itemId,
        final String title,
        final String mainImg,
        final String redirect_url,
        final String ip
    )throws IOException{
        final String url = request.getRequestURL().toString();
        final String ua = request.getHeader("user-agent");
        putMsg(  itemId, title, mainImg, url, ua, ip);
        if (redirect_url != null){
            synchronized(botua_matcher){
                botua_matcher.reset(ua);
                if (!botua_matcher.find()){
                    response.sendRedirect(redirect_url);
                }
            }
        }
    }

    /**
     *消息监控入口
     * @param itemId
     * @param title
     * @param mainImg
     * @param url
     * @param ua
     * @param ip
     */
    public static void putMsg( final String itemId,
                                             final String title,
                                             final String mainImg,
                                             final String url,
                                             final String ua,
                                             final String ip){
        Const.es.submit(new Runnable() {
            @Override
            public void run() {
                final Visitor v = new Visitor();
                v.remoteIp=ip;
                v.time=System.currentTimeMillis();
                v.visitNum=++visitNum;
                v.url=url;
                v.itemId=itemId;
                v.title=title;
                v.mainImg=getMainImgURL(  url,   mainImg);
                v.user_agent=ua;
                //传输到监控
                try{VisitorEndpoint.putMsg(v);}catch (Exception e1){e1.printStackTrace();}
                final StringBuilder sb = new StringBuilder();
                sb.append("[")
                        .append(Const.getTime(new Date(v.time)))
                        .append("]\nvisitNum:")
                        .append(v.visitNum)
                        .append("\nurl:")
                        .append(url)
                        .append("\nremoteIp:")
                        .append(v.remoteIp)
                        .append("\nitemId:")
                        .append(itemId)
                        .append("\ntitle:")
                        .append(title)
                        .append("\nmainImg:")
                        .append(v.mainImg)
                        .append("\nuser-agent:")
                        .append(ua)
                        .append("\n");
                //打印到logging
                System.out.print(sb.toString());
            }
        });
    }


    private static String getMainImgURL(String url, String mainImg){
        int idx=url.lastIndexOf('/');
        StringBuilder sb=new StringBuilder();
        sb.append(url.substring(0,idx+1));
        sb.append( mainImg);
        return sb.toString();
    }

    //获取ip
    public static String getIPAddress(HttpServletRequest request) {
        String ip = null;

        //X-Forwarded-For：Squid 服务代理
        String ipAddresses = request.getHeader("X-Forwarded-For");

        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            //Proxy-Client-IP：apache 服务代理
            ipAddresses = request.getHeader("Proxy-Client-IP");
        }

        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            //WL-Proxy-Client-IP：weblogic 服务代理
            ipAddresses = request.getHeader("WL-Proxy-Client-IP");
        }

        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            //HTTP_CLIENT_IP：有些代理服务器
            ipAddresses = request.getHeader("HTTP_CLIENT_IP");
        }

        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            //X-Real-IP：nginx服务代理
            ipAddresses = request.getHeader("X-Real-IP");
        }

        //有些网络通过多层代理，那么获取到的ip就会有多个，一般都是通过逗号（,）分割开来，并且第一个ip为客户端的真实IP
        if (ipAddresses != null && ipAddresses.length() != 0) {
            ip = ipAddresses.split(",")[0];
        }

        //还是不能获取到，最后再通过request.getRemoteAddr();获取
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

}
