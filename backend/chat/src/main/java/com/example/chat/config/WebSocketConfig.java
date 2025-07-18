package com.example.chat.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.sockjs.transport.handler.WebSocketTransportHandler;
import org.springframework.web.socket.sockjs.transport.handler.XhrPollingTransportHandler;

import com.example.chat.service.UserDetailServiceImp;
import com.example.chat.util.JwtUtil;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer  {

    private final JwtUtil jwtUtil;
    private final UserDetailServiceImp userDetailService;
    private final JwtHandshakeInterceptor jwtHandshakeInterceptor;


    public WebSocketConfig(JwtUtil jwtUtil, UserDetailServiceImp userDetailService,
            JwtHandshakeInterceptor jwtHandshakeInterceptor) {
        this.jwtUtil = jwtUtil;
        this.userDetailService = userDetailService;
        this.jwtHandshakeInterceptor = jwtHandshakeInterceptor;
    }

    // Endpoint clients connect to (React will use this)
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/chat")
                .setAllowedOrigins("http://localhost:5173") // You can replace * with your frontend URL in prod
                .addInterceptors(jwtHandshakeInterceptor)
                .withSockJS();
    }

     // Define routes and prefixes for messages
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic/room");     // For outgoing messages
        config.setApplicationDestinationPrefixes("/app"); // For incoming messages
    }

     @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor =
                        MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

                if (accessor != null && accessor.getCommand() != null) {
                    String tokenHeader = accessor.getFirstNativeHeader("Authorization");

                    if (tokenHeader != null && tokenHeader.startsWith("Bearer ")) {
                        String token = tokenHeader.substring(7);
                        String username = jwtUtil.extractUsername(token);

                        if (username != null && jwtUtil.validateToken(token, username)) {
                            var userDetails = userDetailService.loadUserByUsername(username);
                            var auth = new UsernamePasswordAuthenticationToken(
                                    userDetails, null, userDetails.getAuthorities());

                            // Set authentication to WebSocket session
                            accessor.setUser(auth);
                            SecurityContextHolder.getContext().setAuthentication(auth);
                        }
                    }
                }

                return message;
            }
        });
    }

}
