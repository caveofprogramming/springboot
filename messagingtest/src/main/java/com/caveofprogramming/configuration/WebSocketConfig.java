package com.caveofprogramming.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/chat").setAllowedOrigins("*").withSockJS()
        .setClientLibraryUrl("/js/sockjs.min.js");
        
        // Need to set the client library location in case socks falls back to using
        // iframes, which would then otherwise get downloaded by default from a CDN,
        // possibly causing cross-origin request blocking.
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
    	registry.enableSimpleBroker("/queue");
    	registry.setApplicationDestinationPrefixes("/app");
    }
    

    
}
