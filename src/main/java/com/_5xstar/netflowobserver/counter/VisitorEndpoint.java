package com._5xstar.netflowobserver.counter;

import com._5xstar.netflowobserver.ipaddrquerier.Ip2AddrService;
import com.alibaba.fastjson2.JSON;
import org.apache.tomcat.websocket.WsIOException;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.Closeable;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

@ServerEndpoint(value = "/wsapi" )
public class VisitorEndpoint implements Closeable {

	// 记录当前有多少个用户加入到了聊天室，它是static全局变量。为了多线程安全使用原子变量AtomicInteger
	private static final AtomicInteger connectionIds = new AtomicInteger(0);

	//每个用户用一个CharAnnotation实例来维护，请你注意它是一个全局的static变量，所以用到了线程安全的CopyOnWriteArraySet
	private static final Set<VisitorEndpoint> connections = new CopyOnWriteArraySet<>();

	//
	private static final Map<String, String> wsids = new HashMap<>();
	public static synchronized String doWsid(final boolean isPut, final String wsid, final String name){
		if(isPut) {
			wsids.put(wsid, name);
			return null;
		}else{
			return wsids.remove(wsid);
		}
	}

	private final int id;
	private String name;
	private Session session;
	private String wsid;

	public VisitorEndpoint() {
		id =  connectionIds.getAndIncrement();
	}

	//新连接到达时，Tomcat会创建一个Session，并回调这个函数
	@OnOpen
	public void start(Session session)throws WsIOException {
		System.out.println("ws login session="+session);  //test
		final List<String> ids = session.getRequestParameterMap().get("wsid");
		String user;
		if(ids!=null && !ids.isEmpty()
				&& (user=doWsid(false, ids.get(0), null))!=null ){
			this.name=user;
			this.session=session;
			this.wsid=ids.get(0);
			connections.add(this);
			String message = String.format("* %s (%d) %s", name, id, "进入。");
			broadcast(message);
			doMsg(false, null);
		}else{
			throw new WsIOException(new CloseReason(CloseReason.CloseCodes.VIOLATED_POLICY, "id为空或不匹配！"));
		}
	}

	//浏览器关闭连接时，Tomcat会回调这个函数
	@OnClose
	public void end() {
		connections.remove(this);
		wsids.remove(this.wsid);
		String message = String.format("* %s (%d) %s", name, id,  "已离开。");
		broadcast(message);
	}

	//实现Closeable
	@Override
	public void close(){
		end();
	}

	//浏览器发送消息到服务器时，Tomcat会回调这个函数
	@OnMessage
	public void incoming(String message) {
		// Never trust the client
		String filteredMessage = String.format("%s: %s",
				name, message.replaceAll("<[^>]*>", ""));
		broadcast(filteredMessage);
	}

	// WebSocket连接出错时，Tomcat会回调这个函数
	@OnError
	public void onError(Throwable t) throws Throwable {
		System.out.println("聊天室发生错误：" + t.toString());
	}

	// 向聊天室中的每个用户广播消息
	public static void broadcast(String msg) {
		System.out.println(msg);  //测试
		for (VisitorEndpoint client : connections) {
			//try {
				synchronized (client) {
					//client.session.getBasicRemote().sendText(msg);
					client.session.getAsyncRemote().sendText(msg);
				}
			//} catch (IOException e) {
             //  e.printStackTrace();
			//}
		}
	}

	//主动离开
	public static void leave(final String user){
		if(user==null || connections.isEmpty())return;
		for (VisitorEndpoint client : connections) {
			if(user.equals(client.name)) {
				synchronized (client) {
					client.end();
					break;
				}
			}
		}
	}

	/**
	 * 发布流量
	 * @param msg
	 */
	public static void putMsg(final ListenItem msg)throws IOException {
		doMsg(true, msg);
		if(!connections.isEmpty()) {
			String s;
			if (msg.getAddr() == null && (s = Ip2AddrService.getIp2AddrString(msg.getIp())) != null) {
				msg.setAddr(s);   //填入地址
			}
			final ArrayList<ListenItem> items = new ArrayList<>(1);
			items.add(msg);
			broadcast(JSON.toJSONString(items));
		}
	}

	//消息缓存
	final private static ListenItem[] buf=new ListenItem[Const.maxMsgLength];
	private static int index = 0;      //缓存位置

	//把消息发送到等待的连接
	private  static synchronized void doMsg(final boolean isPut, final ListenItem msg){
		if(isPut){
			final int idx = index++ % Const.maxMsgLength;
			buf[idx] = msg;
		}else {
			final ArrayList<ListenItem> items = new ArrayList<>(Const.maxMsgLength);
			int start = index % Const.maxMsgLength;
			for (int i = start; i < Const.maxMsgLength; i++) {
				if (buf[i] != null)items.add(buf[i]);
			}
			for (int i = 0; i < start; i++) {
				if (buf[i] != null)items.add(buf[i]);
			}
			if (!items.isEmpty()){
				start = 0;
				int end=start+Const.pageSize;
				final int size = items.size();
				String s;
				for( ; start < size;  ) {
					if(end > size)end=size;
					List<ListenItem> pitems =  items.subList(start, end);
					for (ListenItem item : pitems) {
						if(item.getAddr()==null && (s=Ip2AddrService.getIp2AddrString(item.getIp()))!=null) {
							item.setAddr(s);   //填入地址
						}
					}
					broadcast(JSON.toJSONString(pitems));
					//进行下一页
					start = end;
					end=start+Const.pageSize;
				}
			}
		}
	}
}

/**
@ServerEndpoint("/vwslogin")
public class VisitorWsLogin {
	
	@OnOpen
	public void onOpen(){
		System.out.println("vwslogin ...");
		//System.out.println(servletContext);
	}
	
	@OnClose
	public void onClose(){
		System.out.println("Close Connection ...");
	}
	
	@OnMessage
	public String onMessage(String message){
		System.out.println("Message from the client: " + message);
		String echoMsg = "Echo from the server : " + message;
		return echoMsg;
	}

	@OnError
	public void onError(Throwable e){
		e.printStackTrace();
	}

}
**/