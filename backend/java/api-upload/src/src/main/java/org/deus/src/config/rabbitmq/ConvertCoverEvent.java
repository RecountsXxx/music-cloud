package org.deus.src.config.rabbitmq;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConvertCoverEvent {
    @Bean
    public Queue convertCover() {
        return new Queue("convert.cover", true);
    }
}
