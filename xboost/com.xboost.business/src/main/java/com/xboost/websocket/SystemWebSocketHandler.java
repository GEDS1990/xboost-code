package com.xboost.websocket;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import com.xboost.pojo.User;
import com.xboost.util.ShiroUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

public class SystemWebSocketHandler implements WebSocketHandler {

    private Logger log = LoggerFactory.getLogger(SystemWebSocketHandler.class);

    private static final ArrayList<WebSocketSession> users = new ArrayList<WebSocketSession>();;


    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("ConnectionEstablished");
        log.debug("ConnectionEstablished");
        users.add(session);

//        session.sendMessage(new TextMessage("connect"));
//        session.sendMessage(new TextMessage("new_msg"));
        session.sendMessage(new TextMessage("connected"));

    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        System.out.println("handleMessage" + message.toString());
        log.debug("handleMessage" + message.toString());
        //sendMessageToUsers();
        session.sendMessage(new TextMessage(new Date() + ""));
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        if(session.isOpen()){
            session.close();
        }
        users.remove(session);

        log.debug("handleTransportError" + exception.getMessage());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        users.remove(session);
        log.debug("afterConnectionClosed" + closeStatus.getReason());

    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
    /**
     * 给某个用户发送消息
     *
     * @param userName
     * @param message
     */
    public void sendMessageToUser(String userName, TextMessage message) {

        for (WebSocketSession user : users) {
//            Principal principal = user.getPrincipal();
//            user.webSocketSession.user.object.username;
            try {
                if (user.isOpen()) {
                    user.sendMessage(message);
                }else{
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * 给所有在线用户发送消息
     *
     * @param message
     */
    public void sendMessageToUsers(TextMessage message) {
        for (WebSocketSession user : users) {
            try {
                if (user.isOpen()) {
                    user.sendMessage(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
