package vnpay.vn.harabbit.core;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

@Slf4j
public class ExchangeChannelFactory {
    private static Channel channel;

    public ExchangeChannelFactory() throws IOException {
        if (Objects.isNull(channel)) {
            ChannelPool channelPool = new ChannelPool();
            channel = channelPool.getChannel();
        }
    }

    public void declareExchange(BuiltinExchangeType exchangeType, String... exchangeNames) throws IOException {
        log.info("method declareExchange() START with exchangeNames {}", (Object) exchangeNames);
        for (String exchangeName : exchangeNames) {
            // exchangeDeclare( exchange, builtinExchangeType, durable)
            channel.exchangeDeclare(exchangeName, exchangeType, true);
        }
        log.info("method declareExchange() END");
    }


    public void declareQueues(String... queueNames) throws IOException {
        log.info("method declareQueues() START with queueNames {}", (Object) queueNames);
        for (String queueName : queueNames) {
            // queueDeclare  - (queueName, durable, exclusive, autoDelete, arguments)
            channel.queueDeclare(queueName, true, false, false, null);
        }
        log.info("method declareQueues() END");
    }

    public void performQueueBinding(String exchangeName, String queueName, String routingKey) throws IOException {
        // Create bindings - (queue, exchange, routingKey)
        channel.queueBind(queueName, exchangeName, routingKey);
    }

    public String subscribeMessage(String queueName) throws IOException {
        log.info("method subscribeMessage() START with queueName {}", queueName);
        final String[] result = {""};
        // basicConsume - ( queue, autoAck, deliverCallback, cancelCallback)
        channel.basicConsume(queueName, true, ((consumerTag, message) -> {
            result[0] = Arrays.toString(message.getBody());
        }), consumerTag -> {
        });
        log.info("method subscribeMessage() END");
        return result[0];
    }

    public void publishMessage(String exchangeName, String message, String routingKey) throws IOException {
        log.info("method publishMessage() START with message {}", message);
        // basicPublish - ( exchange, routingKey, basicProperties, body)
        channel.basicPublish(exchangeName, routingKey, null, message.getBytes());
        log.info("method publishMessage()  [Send] routing-key {} with message {} ", routingKey, message);
        log.info("method publishMessage() END with message {}", message);
    }
}
