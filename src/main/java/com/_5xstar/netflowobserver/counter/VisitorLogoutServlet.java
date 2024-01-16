package com._5xstar.netflowobserver.counter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/** 
 * 登出
 * 庞海文  2023-12-25
 */
public final class VisitorLogoutServlet extends HttpServlet{

	public VisitorLogoutServlet() {
		super();
	}
	public void doGet(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException{
		this.doPost(request, response);
	}
	public void doPost(HttpServletRequest request,HttpServletResponse response) throws  IOException{
		       VisitorEndpoint.leave( request.getParameter("username"));  //移除聊天室
			   response.sendRedirect("/llik/login.html");
	}



}