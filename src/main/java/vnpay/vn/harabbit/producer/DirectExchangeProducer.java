package vnpay.vn.harabbit.producer;

import com.rabbitmq.client.BuiltinExchangeType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import vnpay.vn.harabbit.constant.Constant;
import vnpay.vn.harabbit.core.ExchangeChannelFactory;

import java.io.IOException;

@Slf4j
@Component
public class DirectExchangeProducer {
    private ExchangeChannelFactory channel;

    public void start() throws IOException {
        log.info("DirectExchangeProducer method start() START");
        // Create channel
        channel = new ExchangeChannelFactory();

        // Create direct exchange
        channel.declareExchange(BuiltinExchangeType.DIRECT, Constant.EXCHANGE);
        channel.declareExchangeWithAlterExchange(BuiltinExchangeType.DIRECT, Constant.ALTER_EXCHANGE);
        // Create queues
        channel.declareQueues(Constant.QUEUE);
        channel.declareQueues(Constant.ALTER_QUEUE);

        // Binding queues
        channel.performQueueBinding(Constant.EXCHANGE,
                Constant.QUEUE,
                Constant.ROUTING_KEY);
        channel.performQueueBinding(Constant.ALTER_EXCHANGE,
                Constant.ALTER_QUEUE,
                Constant.ROUTING_KEY);
        log.info("DirectExchangeProducer method start() END");
    }

    public void send(String exchangeName, String message, String routingKey) throws IOException {
        log.info("DirectExchangeProducer method send() START");
        // Send message
        channel.publishMessage(exchangeName, message, routingKey);
        log.info("DirectExchangeProducer method send() END");
    }
}
