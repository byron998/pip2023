package com.ibm.webdev.app.websocket;

import org.springframework.web.socket.WebSocketSession;

public class WebSocketStoreBean {
    private WebSocketSession webSocketSession;
    private int clientId;
    
	public WebSocketSession getWebSocketSession() {
		return webSocketSession;
	}
	public void setWebSocketSession(WebSocketSession webSocketSession) {
		this.webSocketSession = webSocketSession;
	}
	public int getClientId() {
		return clientId;
	}
	public void setClientId(int clientId) {
		this.clientId = clientId;
	}
    
}
