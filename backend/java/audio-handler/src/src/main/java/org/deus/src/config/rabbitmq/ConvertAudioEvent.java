package org.deus.src.config.rabbitmq;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConvertAudioEvent {
    @Bean
    public Queue convertAudio() {
        return new Queue("convert.audio", true);
    }
}
