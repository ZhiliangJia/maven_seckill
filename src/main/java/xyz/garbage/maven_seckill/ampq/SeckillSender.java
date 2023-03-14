package xyz.garbage.maven_seckill.ampq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.garbage.maven_seckill.util.SerializeUtil;

@Service
public class SeckillSender {

    private final Logger log = LoggerFactory.getLogger(SeckillSender.class);

    @Autowired
    AmqpTemplate amqpTemplate;

    public void sendSeckillMessage(Object message) {
        String msg = SerializeUtil.beanToString(message);
        log.info("Push message into RabbitMQ: {}", msg);
        amqpTemplate.convertAndSend(msg);
    }

}
