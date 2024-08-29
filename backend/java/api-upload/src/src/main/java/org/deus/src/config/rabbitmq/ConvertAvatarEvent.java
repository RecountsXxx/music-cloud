package org.deus.src.config.rabbitmq;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConvertAvatarEvent {
    @Bean
    public Queue convertAvatar() {
        return new Queue("convert.avatar", true);
    }
}
