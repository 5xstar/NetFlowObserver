package com._5xstar.netflowobserver.ipaddrquerier;


import java.io.Serializable;

/**
 * 从外网查询的数据载体
 * 庞海文 2024-1-4
**/

public interface Ip2AddrData extends Serializable
{

	//点击时间
	long getTime();

	// 国家
	default String getCountry( ){return null;}

	// 省
	default String getProvince( ){return null;}

	// 市
	default String getCity( ){return null;}

	//区
	default String getTown( ){return null;}

	//门牌
	default String getAddress( ){return null;}

	//网络供应商
	default String getNet(){return null;}

	default String getDesc(){
		final StringBuilder sb = new StringBuilder();
		String s = getCountry();
		if(s!=null && !"".equals(s) )sb.append(s).append("  ");
		s = getProvince();
		if(s!=null && !"".equals(s) )sb.append('/').append(s);
		s = getCity();
		if(s!=null && !"".equals(s) )sb.append('/').append(s);
		s = getTown();
		if(s!=null && !"".equals(s) )sb.append('/').append(s);
		s = getAddress();
		if(s!=null && !"".equals(s) )sb.append('/').append(s);
		s = getNet();
		if(s!=null && !"".equals(s) )sb.append("  ").append(s);
		return sb.toString();
	}

}

