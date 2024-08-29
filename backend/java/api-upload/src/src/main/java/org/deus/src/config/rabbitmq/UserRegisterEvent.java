package org.deus.src.config.rabbitmq;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserRegisterEvent {
    @Bean
    public Queue userRegister() {
        return new Queue("user.register", true);
    }
}
