package vnpay.vn.harabbit.core;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import java.io.IOException;

/**
 * @author sontt1
 * Date:9/28/2022
 * Time:4:06 PM
 */
@Slf4j
public class RabbitMQPool {
    private final Connection connection;

    public RabbitMQPool() {
        this.connection = RabbitMQ.getInstance().getConnection();
    }

    private static final class SingletonHolder {

        private static final RabbitMQPool INSTANCE = new RabbitMQPool();
    }

    public static RabbitMQPool getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private GenericObjectPool<Channel> pool;

    @Setter
    @Getter
    private int maxChannel = 200;

    /**
     *
     */
    public void start() {
        try {
            GenericObjectPoolConfig config = new GenericObjectPoolConfig();
            config.setMaxTotal(maxChannel);
            config.setBlockWhenExhausted(true);
            config.setMaxWaitMillis(60000);
            config.setTestOnBorrow(true);
            config.setTestOnReturn(true);
            config.setNumTestsPerEvictionRun(15);
            pool = new GenericObjectPool<>(new RabbitChannelFactory(connection), config);
            log.info("Start rabbit channel pool success.");
        } catch (Exception ex) {
            log.error("RabbitMQPool have ex: ", ex);
        }
    }

    /**
     * @return @throws Exception
     */
    public Channel borrowChannel() throws Exception {
        log.info("Method borrowChannel() START");
        if (null == pool) {
            return null;
        }
        Channel channel;
        try {
            channel = pool.borrowObject();
            log.info("Method borrowChannel() END");
        } catch (IllegalStateException ex) {
            log.error("BorrowChannel have ex: ", ex);
            checkConnectionAndChannelPool();
            channel = pool.borrowObject();
        }
        if (isInvalidChannel(channel)) {
            log.warn("Got a closed channel from the pool. Invalidating and borrowing a new one from the pool.");
            pool.invalidateObject(channel);
            checkConnectionAndChannelPool();
            channel = pool.borrowObject();
        }
        return channel;
    }

    /**
     *
     */
    private synchronized void checkConnectionAndChannelPool() throws IOException {
        if (null != pool) {
            try {
                pool.close();
                pool = null;
            } catch (Exception e) {
                throw new IOException("Error closing channelPool", e);
            }
            start();
        }
    }

    /**
     * @param channel
     */
    public void returnChannel(Channel channel) {
        if (channel != null) {
            pool.returnObject(channel);
        }
    }

    /**
     *
     */
    public void close() {
        if (pool != null) {
            pool.close();
            pool = null;
        }
    }

    /**
     * @param channel
     * @return
     */
    private boolean isInvalidChannel(Channel channel) {
        return null == channel || !channel.isOpen();
    }
}
