package com._5xstar.netflowobserver.counter;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.File;
import java.security.SecureRandom;
import java.util.Properties;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.net.URL;

/** 
 * 登录，需要验证
 * 庞海文  2023-12-25
 */
public final class VisitorLoginServlet extends HttpServlet {

	public VisitorLoginServlet() {
		super();
	}
	public void doGet(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException{
		this.doPost(request, response);
	}
	public void doPost(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException{
		   if(validate(request, response)){
			   response.setStatus(200);
			   //response.sendRedirect("lljk.jsp");
		   }else{
			   //非法登录终止
			   response.setStatus(403);
			   //response.sendRedirect("error.jsp");
		   }
	}
	private static boolean validate(final HttpServletRequest request, final HttpServletResponse response ){
		final String user = request.getParameter("username");
		String p;
		if(user!=null && (p = getPassword(  request,   user))!=null){
			final String password =  request.getParameter("password");
			if(password!=null && p.equals(password)){
				//生成登录id
				try {
					final String wsid = createWsid();
					System.out.println("wsid="+wsid);
					VisitorEndpoint.doWsid(true, wsid, user);
					String setHeaderCookieStr = "wsid=" + wsid ;
					if (request.isSecure()) {
						setHeaderCookieStr += "; Secure" ;
					}
					response.setHeader("Set-Cookie", setHeaderCookieStr);
					//response.addCookie(new Cookie("wsid", wsid));
					//request.setAttribute("wsid", wsid);
					//response.addCookie(new Cookie("username", user));
					//request.setAttribute("username", user);
					System.out.println("user="+user+" password=" + p+" wsid="+wsid);
					/*String wsid = request.getParameter("wsid");
					String username = request.getParameter("username");
					if(wsid==null || username == null){
						Cookie[] cks = request.getCookies();
						if(cks!=null && cks.length>0){
							String n;
							for(int i=0; i<cks.length; i++) {
								n = cks[i].getName();
								if(n.equals("username"))wsid=cks[i].getValue();
								else if(n.equals("username"))username=cks[i].getValue();
							}
						}
					}*/
					return true;
				}catch (Exception e){
					e.printStackTrace();
				}
			}
		}
		return false;
	}

	/**
	 * 创建wsid
	 * @return
	 * @throws Exception
	 */
	private static String createWsid()throws Exception{
		System.out.println("enter createwsid");
		final SecureRandom sr = new SecureRandom();
		final byte[] d = new byte[64];
		sr.nextBytes(d);
		final StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 64; i++) {
			sb.append(String.format("%02X", d[i]));
		}
		return sb.toString();
	}


	/**
	 * 获取用户密码
	 * @param request
	 * @param user
	 * @return
	 */
	private static String getPassword(final HttpServletRequest request, final String user){
		File f=null;
		final URL url = VisitorLoginServlet.class.getResource("users.properties");
		if(url!=null)f = new File(url.getFile());
		if(f==null || !f.exists()) {
			final HttpSession session = request.getSession();
			final ServletContext servletContext = session.getServletContext();
			String path = servletContext.getRealPath("WEB-INF");
			System.out.println("path=" + path);
			f = new File(path, "users.properties");
			if(!f.exists()) {
				path = servletContext.getRealPath("WEB-INF/classes");
				System.out.println("path=" + path);
				f = new File(path, "users.properties");
			}
		}
		System.out.println(f.getAbsolutePath());
		try {
			if (f.exists()) {
				Properties p = new Properties();
				p.load(new FileInputStream(f));
				return (String) p.getProperty(user);
			}
		}catch (IOException ioe){
			ioe.printStackTrace();
		}
		return null;
	}


}