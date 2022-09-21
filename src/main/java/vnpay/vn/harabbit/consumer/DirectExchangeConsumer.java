package vnpay.vn.harabbit.consumer;

import com.rabbitmq.client.BuiltinExchangeType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import vnpay.vn.harabbit.constant.Constant;
import vnpay.vn.harabbit.core.ExchangeChannelFactory;

import java.io.IOException;

@Slf4j
@Component
public class DirectExchangeConsumer {
    private ExchangeChannelFactory channel;

    public void start() throws IOException {
        log.info("DirectExchangeConsumer method start() START");
        // Create channel
        channel = new ExchangeChannelFactory();

        // Create direct exchange
        channel.declareExchange(BuiltinExchangeType.DIRECT, Constant.EXCHANGE);
        // Create queues
        channel.declareQueues(Constant.QUEUE);

        // Binding queues
        channel.performQueueBinding(Constant.EXCHANGE,
                Constant.QUEUE,
                Constant.ROUTING_KEY);
        log.info("DirectExchangeConsumer method start() END");
    }

    public String subscribe() throws IOException {
        log.info("DirectExchangeConsumer method subscribe() START");
        // Subscribe message
        log.info("DirectExchangeConsumer method subscribe() END");
        return channel.subscribeMessage(Constant.QUEUE);
    }
}
