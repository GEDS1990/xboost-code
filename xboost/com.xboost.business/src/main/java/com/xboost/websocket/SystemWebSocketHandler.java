package com.xboost.websocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.CopyOnWriteArraySet;

import com.xboost.service.MyScenariosService;
import com.xboost.util.ShiroUtil;
import com.xboost.util.SpringBeanFactoryUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
public class SystemWebSocketHandler implements WebSocketHandler {

    private Logger logger = LoggerFactory.getLogger(SystemWebSocketHandler.class);
    private MyScenariosService myScenariosService =
            (MyScenariosService)SpringBeanFactoryUtil.getBean("myScenariosService");

//    private static final ArrayList<WebSocketSession> users = new ArrayList<WebSocketSession>();;
    private static CopyOnWriteArraySet<WebSocketSession> users = new CopyOnWriteArraySet<WebSocketSession>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
//        System.out.println("ConnectionEstablished");
//        log.debug("ConnectionEstablished");
        users.add(session);

//        session.sendMessage(new TextMessage("connect"));
//        session.sendMessage(new TextMessage("new_msg"));
//        session.sendMessage(new TextMessage("connected"));

    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
//        System.out.println("handleMessage" + message.toString());
//        log.debug("handleMessage" + message.toString());
        //sendMessageToUsers();
        session.sendMessage(new TextMessage(new Date() + ""));
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        if(session.isOpen()){
            session.close();
        }
        users.remove(session);

//        log.debug("handleTransportError" + exception.getMessage());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        users.remove(session);
//        log.debug("afterConnectionClosed" + closeStatus.getReason());

    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
    /**
     * 给某个用户发送消息
     *
     * @param message
     */
    public void sendMessageToUser(TextMessage message) {
        for (WebSocketSession user : users) {
//            if (user.getAttributes().get("WEBSOCKET_USERID").equals(ShiroUtil.getCurrentUserId())) {
                try {
                    if (user.isOpen()) {
                        user.sendMessage(message);
                        ShiroUtil.setSimulateConsole(message.getPayload());
                        logger.info(message.getPayload());
                    }
                } catch (IOException e) {
//                    e.printStackTrace();
                    myScenariosService.updateStatus("Editable");
                }
//                break;
//            }
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
//                e.printStackTrace();
            }
        }
    }

}
