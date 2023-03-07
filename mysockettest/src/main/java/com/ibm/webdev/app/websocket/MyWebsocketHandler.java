package com.ibm.webdev.app.websocket;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

public class MyWebsocketHandler extends AbstractWebSocketHandler {

	public static final Map<String, WebSocketStoreBean> webSocketBeanMap;
	public static final Map<String, BigDecimal> webSocketMovieMap;
 
	private final Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 仅用用于标识客户端编号
	 */
	private static final AtomicInteger clientIdMaker;

	static {
		webSocketBeanMap = new ConcurrentHashMap<>();
		webSocketMovieMap = new ConcurrentHashMap<>();
		clientIdMaker = new AtomicInteger(0);
	}

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		// 当WebSocket连接正式建立后，将该Session加入到Map中进行管理
		WebSocketStoreBean webSocketBean = new WebSocketStoreBean();
		webSocketBean.setWebSocketSession(session);
		webSocketBean.setClientId(clientIdMaker.getAndIncrement());
		webSocketBeanMap.put(session.getId(), webSocketBean);
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		// 当连接关闭后，从Map中移除session实例
		webSocketBeanMap.remove(session.getId());
	}

	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
		// 传输过程中出现了错误
		if (session.isOpen()) {
			session.close();
		}
		webSocketBeanMap.remove(session.getId());
	}

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		// 处理接收到的消息
		logger.info("Received message from client[ID:" + webSocketBeanMap.get(session.getId()).getClientId()
				+ "]; Content is [" + message.getPayload() + "].");
		if (session.getAttributes().get("type").equals("ws-publisher")) {
			// 处理发布消息
			TextMessage textMessage = new TextMessage("Server has received your message.");
			String[] messarr = message.getPayload().split(":");
			String movieName = messarr[0];
			int price = (messarr.length > 1) ? Integer.valueOf(messarr[1]) : 0;
			BigDecimal db;
			// income price ++
			if (webSocketMovieMap.containsKey(movieName)) {
				db = webSocketMovieMap.get(movieName);
				db = db.add(new BigDecimal(price));
				webSocketMovieMap.replace(movieName, db);
			}
			else {
				db = new BigDecimal(price);
				webSocketMovieMap.put(movieName, db);
			}
			session.sendMessage(textMessage);
			// 全体广播
			for (String key : webSocketBeanMap.keySet()) {
				if ("ws-reader".equals(webSocketBeanMap.get(key).getWebSocketSession().getAttributes().get("type"))) {
					webSocketBeanMap.get(key).getWebSocketSession().sendMessage(new TextMessage(movieName+":"+db.toString()));
				}
			}
		}
	}
}