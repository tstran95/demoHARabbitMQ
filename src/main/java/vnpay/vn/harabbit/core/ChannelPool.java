package vnpay.vn.harabbit.core;

import com.rabbitmq.client.Channel;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import java.util.NoSuchElementException;

/**
 * @author sontt1
 * Date:9/13/2022
 * Time:9:32 AM
 */
public class ChannelPool implements Cloneable {
    private GenericObjectPool<Channel> internalPool;
    public static GenericObjectPoolConfig defaultConfig;

    static {
        defaultConfig = new GenericObjectPoolConfig();
        defaultConfig.setMinIdle(5);
        defaultConfig.setMaxTotal(10_000);
        defaultConfig.setMaxTotal(10_000);
        defaultConfig.setBlockWhenExhausted(false);
    }

    public ChannelPool() {
        this(defaultConfig, new ChannelFactory());
    }

    public ChannelPool(final GenericObjectPoolConfig poolConfig, ChannelFactory factory) {
        if (this.internalPool != null) {
            try {
                closeInternalPool();
            } catch (Exception e) {
            }
        }

        this.internalPool = new GenericObjectPool<Channel>(factory, poolConfig);
    }

    private void closeInternalPool() {
        try {
            internalPool.close();
        } catch (Exception e) {
            throw new RuntimeException("Could not destroy the pool");
        }
    }

    public void returnChannel(Channel channel) {
        try {
            if (channel.isOpen()) {
                internalPool.returnObject(channel);
            } else {
                internalPool.invalidateObject(channel);
            }
        } catch (Exception e) {
            throw new RuntimeException("Could not return the resource to the pool");
        }
    }

    public Channel getChannel() {
        try {
            return internalPool.borrowObject();
        } catch (NoSuchElementException nse) {
            if (null == nse.getCause()) { // The exception was caused by an exhausted pool
                throw new RuntimeException("Could not get a resource since the pool is exhausted");
            }
            // Otherwise, the exception was caused by the implemented activateObject() or ValidateObject()
            throw new RuntimeException("Could not get a resource from the pool");
        } catch (Exception e) {
            throw new RuntimeException("Could not get a resource from the pool");
        }
    }
}
