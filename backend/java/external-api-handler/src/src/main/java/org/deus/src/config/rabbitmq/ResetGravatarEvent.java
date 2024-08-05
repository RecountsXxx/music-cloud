package org.deus.src.config.rabbitmq;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ResetGravatarEvent {
    @Bean
    public Queue resetGravatar() {
        return new Queue("reset.gravatar", true);
    }
}
