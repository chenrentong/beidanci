package com.dascom.websocket;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
@ServerEndpoint(value = "/OpenapiWebSocket/{dsAppid}")
public class OpenapiWebSocket {

	private static final Logger logger = LogManager.getLogger(OpenapiWebSocket.class);

	private static ConcurrentHashMap<String, Session> sessionMap = new ConcurrentHashMap<String, Session>();
	
	/**
	 * 连接建立成功调用的方法
	 * 
	 * @param dsAppid
	 * @param session
	 * @throws IOException
	 */
	@OnOpen
	public void onOpen(@PathParam("dsAppid") String dsAppid, Session session)
			throws IOException {
		logger.info("------{}:加入websocket------", dsAppid);
		//设备绑定
		sessionMap.put(dsAppid, session);
	}

	@OnClose
	public void onClose(@PathParam("dsAppid") String dsAppid) {
		logger.info("------{}:退出websocket------", dsAppid);
		sessionMap.remove(dsAppid);
	}

	/**
	 * 发生错误时调用
	 */
	@OnError
	public void onError(Session session, Throwable error) {
		logger.error("------websocket出现错误------");
		error.printStackTrace();
	}

	public void sendMsgByDsAppid(String dsAppid, String msg) {
		logger.info("dsAppid为："+dsAppid);
		Session dsSession = sessionMap.get(dsAppid);
		if (dsSession == null) {
			logger.info("------websocket:{}没有打开连接------", dsAppid);
			return;
		}
		try {
			dsSession.getBasicRemote().sendText(msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}