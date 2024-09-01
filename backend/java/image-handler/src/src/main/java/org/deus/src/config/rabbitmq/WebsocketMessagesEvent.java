package org.deus.src.config.rabbitmq;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebsocketMessagesEvent {
    @Bean
    public Queue websocketMessagesSend() {
        return new Queue("websocket.messages", true);
    }
}
