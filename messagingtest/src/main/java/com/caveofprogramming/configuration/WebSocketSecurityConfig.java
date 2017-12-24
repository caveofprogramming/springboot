package com.caveofprogramming.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;


@Configuration
public class WebSocketSecurityConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {
    @Override
    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
    	messages
    	.simpTypeMatchers(SimpMessageType.CONNECT, 
    			SimpMessageType.HEARTBEAT, 
    			SimpMessageType.UNSUBSCRIBE, 
    			SimpMessageType.DISCONNECT).permitAll()
    	.simpDestMatchers("/app/**").authenticated()
    	.simpSubscribeDestMatchers("/user/**").authenticated()
    	.anyMessage().denyAll();

    }
}
