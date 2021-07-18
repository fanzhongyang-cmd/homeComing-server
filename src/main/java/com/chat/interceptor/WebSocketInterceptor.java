package com.chat.interceptor;

import org.json.JSONObject;
import org.json.JSONException;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import javax.servlet.http.HttpSession;
import java.util.Map;

public class WebSocketInterceptor implements HandshakeInterceptor {
    @Override
    public boolean beforeHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Map<String, Object> map) throws Exception {
        System.out.println("before handShake---");
        if (serverHttpRequest instanceof ServletServerHttpRequest) {
            String INFO = serverHttpRequest.getURI().getPath().split("INFO=")[1];
            if (INFO != null && INFO.length() > 0) {
                ServletServerHttpRequest request = (ServletServerHttpRequest) serverHttpRequest;
                HttpSession session = request.getServletRequest().getSession();
                map.put("user_id", INFO);//将用户id和房间id保存到Session上。
                System.out.println("session attribution: user_id="+session.getAttribute("user_id"));
            }
//            if (INFO != null && INFO.length() > 0) {
//                JSONObject jsonObject = new JSONObject(INFO);
////                String command = jsonObject.getString("command");
//                if (jsonObject != null ) {
//                    System.out.println("INFO="+jsonObject);
//                    //System.out.println("当前session的ID="+ jsonObject.getString("from_id"));
//                    ServletServerHttpRequest request = (ServletServerHttpRequest) serverHttpRequest;
//                    HttpSession session = request.getServletRequest().getSession();
////                    map.put("task_id", jsonObject.getString("task_id"));
//                    map.put("user_id", jsonObject.getString("user_id"));//将用户id和房间id保存到Session上。
//                    System.out.println("session attribution: user_id="+session.getAttribute("user_id"));
//                }
//            }
        }
        return true;
    }
    // 拦截建立WebSocket的请求，进行握手处理，并将INFO的信息保存
    @Override
    public void afterHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Exception e) {
        System.out.println("after handshake");
        System.out.println("进来webSocket的afterHandshake拦截器！");
    }
}