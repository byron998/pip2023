package com.ibm.webdev.app.websocket;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

public class MyWebSocketInterceptor extends HttpSessionHandshakeInterceptor {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        logger.info("[MyWebSocketInterceptor#BeforeHandshake] Request from " + request.getRemoteAddress().getHostString());
        if (request instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest serverHttpRequest = (ServletServerHttpRequest) request;
            String type = serverHttpRequest.getServletRequest().getParameter("type");

            //这里做一个简单的鉴权，只有符合条件的鉴权才能握手成功
            if ("ws-publisher".equals(type) || "ws-reader".equals(type)) {
                // 保存认证用户
                attributes.put("type", type);
                return super.beforeHandshake(request, response, wsHandler, attributes);
            } else {
                logger.error("type错误。token：[{}]！", type, new Exception("type错误"));
                return false;
            }
        }
        return super.beforeHandshake(request, response, wsHandler, attributes);
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception ex) {
        logger.info("[MyWebSocketInterceptor#afterHandshake] Request from " + request.getRemoteAddress().getHostString());
    }
}
