package vnpay.vn.harabbit.producer;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import vnpay.vn.harabbit.constant.Constant;
import vnpay.vn.harabbit.core.RabbitMQPool;

import java.util.HashMap;
import java.util.Map;


/**
 * @author sontt1
 * Date:9/28/2022
 * Time:5:14 PM
 */
@Slf4j
public class Producer {
    private final RabbitMQPool rabbitMQPool;

    public Producer() {
        this.rabbitMQPool = RabbitMQPool.getInstance();
    }

    private static final class SingletonHolder {

        private static final Producer INSTANCE = new Producer();
    }

    public static Producer getInstance() {
        return SingletonHolder.INSTANCE;
    }


    /**
     * @param requestQueueName
     * @param replyQueueName
     * @param message
     * @param tokenKey
     * @return
     */
    public boolean sendToQueue(String requestQueueName,
                               String replyQueueName,
                               String message,
                               String tokenKey) {
        log.info("Begin send message: {} to queue: {}.", message, requestQueueName);
        Channel channel = null;
        try {
            channel = rabbitMQPool.borrowChannel();
            if (isInvalidChannel(channel)) {
                log.info("End send message false by cannot borrow channel from pool.");
                return false;
            }
            AMQP.BasicProperties props = new AMQP.BasicProperties.Builder()
                    .correlationId(tokenKey)
                    .replyTo(replyQueueName)
                    .build();
            channel.basicPublish(Strings.EMPTY, requestQueueName, props, message.getBytes("UTF-8"));
            log.info("End send message success", tokenKey);
            return true;
        } catch (Exception ex) {
            log.error("Send message: {} to queue: {} fail by ex", message, requestQueueName, ex);
            return false;
        } finally {
            rabbitMQPool.returnChannel(channel);
        }
    }

    /**
     * @param requestQueueName
     * @param replyQueueName
     * @param message
     * @param tokenKey
     * @return
     */
    public boolean sendToQueue(String requestQueueName,
                               String replyQueueName,
                               byte[] message,
                               String tokenKey) {
        log.info("Begin send message: {} to queue: {}.", message, requestQueueName);
        Channel channel = null;
        try {
            channel = rabbitMQPool.borrowChannel();
            if (isInvalidChannel(channel)) {
                log.info("End send message false by cannot borrow channel from pool.");
                return false;
            }
            AMQP.BasicProperties props = new AMQP.BasicProperties.Builder()
                    .correlationId(tokenKey)
                    .replyTo(replyQueueName)
                    .build();
            channel.basicPublish(Strings.EMPTY, requestQueueName, props, message);
            log.info("End send message success", tokenKey);
            return true;
        } catch (Exception ex) {
            log.error("Send message: {} to queue: {} fail by ex", message, requestQueueName, ex);
            return false;
        } finally {
            rabbitMQPool.returnChannel(channel);
        }
    }

    /**
     * @param requestQueueName
     * @param message
     * @param tokenKey
     * @return
     */
    public boolean sendToQueue(String requestQueueName,
                               String message,
                               String tokenKey) {
        return sendToQueue(requestQueueName, Strings.EMPTY, message, tokenKey);
    }

    /**
     * @param message
     * @return
     */
    public boolean sendToExchange(String message) {
        log.info("Begin send message: {} to exchangeName: {}.", message, Constant.EXCHANGE);
        Channel channel = null;
        try {
            channel = rabbitMQPool.borrowChannel();
            if (isInvalidChannel(channel)) {
                log.info("End send message false by cannot borrow channel from pool.");
                return false;
            }
            //create exchange
            // exchangeDeclare( exchange, builtinExchangeType, durable)
            channel.exchangeDeclare(Constant.EXCHANGE, BuiltinExchangeType.DIRECT, true);

            //create queue
            Map<String, Object> args = new HashMap<>();
            args.put("x-queue-type", "quorum");
            // queueDeclare  - (queueName, durable, exclusive, autoDelete, arguments)
            channel.queueDeclare(Constant.QUEUE, true, false, false, args);

            //binding
            channel.queueBind(Constant.QUEUE, Constant.EXCHANGE, Constant.ROUTING_KEY);

            channel.basicPublish(Constant.EXCHANGE, Constant.ROUTING_KEY, null, message.getBytes("UTF-8"));
            log.info("End send message success to exchangeName: {}", Constant.EXCHANGE);
            return true;
        } catch (Exception ex) {
            log.error("Send message: {} to exchangeName: {} fail by ex",
                    message, ex);
            return false;
        } finally {
            rabbitMQPool.returnChannel(channel);
        }
    }

    /**
     * @param channel
     * @return
     */
    private static boolean isInvalidChannel(Channel channel) {
        return null == channel || !channel.isOpen();
    }
}
