package com.xboost.websocket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.SockJsServiceRegistration;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebMvc
@EnableWebSocket
public class WebSocketConfig extends WebMvcConfigurerAdapter implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(systemWebSocketHandler(), "/webSocketServer");
        SockJsServiceRegistration sockJsServiceRegistration = registry.addHandler(systemWebSocketHandler(), "/webSocketServer/sockjs").setAllowedOrigins("*").withSockJS();
        sockJsServiceRegistration.setHttpMessageCacheSize(1024000000);
        sockJsServiceRegistration.setSessionCookieNeeded(true);
        sockJsServiceRegistration.setStreamBytesLimit(10240000);
        sockJsServiceRegistration.setWebSocketEnabled(true);

        SockJsServiceRegistration sockJsServiceRegistration2 = registry.addHandler(systemWebSocketHandler(), "/webSocketServer/validate").setAllowedOrigins("*").withSockJS();
        sockJsServiceRegistration2.setHttpMessageCacheSize(1024000000);
        sockJsServiceRegistration2.setSessionCookieNeeded(true);
        sockJsServiceRegistration2.setStreamBytesLimit(10240000);
        sockJsServiceRegistration2.setWebSocketEnabled(true);
//        registry.addHandler(systemWebSocketHandler(),"/webSocketServer").addInterceptors(new WebSocketHandshakeInterceptor());
//        registry.addHandler(systemWebSocketHandler(), "/sockjs/webSocketServer").addInterceptors(new WebSocketHandshakeInterceptor()).withSockJS();
//        registry.addHandler(systemWebSocketHandler(), "/webSocketServer/sockjs").withSockJS();
		 /*registry.addHandler(systemWebSocketHandler(),"/ws").addInterceptors(new WebSocketHandshakeInterceptor());
        registry.addHandler(systemWebSocketHandler(), "/ws/sockjs").addInterceptors(new WebSocketHandshakeInterceptor())
                .withSockJS();*/
    }

    @Bean
    public WebSocketHandler systemWebSocketHandler() {
        return new SystemWebSocketHandler();
    }
}

