package vnpay.vn.harabbit.producer;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import vnpay.vn.harabbit.constant.Constant;
import vnpay.vn.harabbit.core.RabbitMQPool;


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


//    /**
//     * @param message
//     * @return
//     */
//    public boolean sendToQueue(String message) {
//        log.info("Begin send message: {} to queue: {} with tokenKey: {}.", message, Constant.QUEUE);
//        Channel channel = null;
//        try {
//            channel = rabbitMQPool.borrowChannel();
//            if (isInvalidChannel(channel)) {
//                log.info("End send message false by cannot borrow channel from pool.");
//                return false;
//            }
////            AMQP.BasicProperties props = new AMQP.BasicProperties.Builder()
////                    .correlationId(tokenKey)
////                    .replyTo(replyQueueName)
////                    .build();
//            channel.basicPublish(Constant.EXCHANGE, Constant.ROUTING_KEY, null, message.getBytes("UTF-8"));
//            log.info("End send message success");
//            return true;
//        } catch (Exception ex) {
//            log.error("Send message: {} to queue: {} fail by ex", message, Constant.EXCHANGE, ex);
//            return false;
//        } finally {
//            rabbitMQPool.returnChannel(channel);
//        }
//    }

    /**
     * @param message
     * @return
     */
    public boolean sendToQueue(byte[] message) {
        log.info("Begin send message: {} to queue: {}.", message, Constant.EXCHANGE);
        Channel channel = null;
        try {
            channel = rabbitMQPool.borrowChannel();
            if (isInvalidChannel(channel)) {
                log.info("End send message false by cannot borrow channel from pool.");
                return false;
            }
//            AMQP.BasicProperties props = new AMQP.BasicProperties.Builder()
//                    .correlationId(tokenKey)
//                    .replyTo(replyQueueName)
//                    .build();
            channel.basicPublish(Constant.EXCHANGE, Constant.ROUTING_KEY, null, message);
            log.info("End send message success");
            return true;
        } catch (Exception ex) {
            log.error("Send message: {} to queue: {} fail by ex", message, Constant.EXCHANGE, ex);
            return false;
        } finally {
            rabbitMQPool.returnChannel(channel);
        }
    }

    /**
     * @param message
     * @return
     */
    public boolean sendToQueue(String message) {
        return sendToQueue(message);
    }

    /**
     * @param message
     * @return
     */
    public boolean sendToExchange(String message) {
        log.info("Begin send message: {} to exchangeName: {}.", message , Constant.EXCHANGE);
        Channel channel = null;
        try {
            channel = rabbitMQPool.borrowChannel();
            if (isInvalidChannel(channel)) {
                log.info("End send message false by cannot borrow channel from pool.");
                return false;
            }
            channel.basicPublish(Constant.EXCHANGE, Constant.ROUTING_KEY, null, message.getBytes("UTF-8"));
            log.info("End send message success to exchangeName: {}", Constant.EXCHANGE);
            return true;
        } catch (Exception ex) {
            log.error("Send message: {} to exchangeName: {} fail by ex",
                    message, Constant.EXCHANGE, ex);
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
