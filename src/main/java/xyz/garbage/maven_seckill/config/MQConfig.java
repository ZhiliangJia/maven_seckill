package xyz.garbage.maven_seckill.config;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MQConfig {
    public final String queueName;

    public MQConfig(@Value("${rabbitmq.queue.name}") String name) {
        this.queueName = name;
    }

    @Bean
    public Queue queue() {
        return new Queue(queueName, true);
    }
}
