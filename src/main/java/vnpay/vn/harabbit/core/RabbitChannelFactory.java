package vnpay.vn.harabbit.core;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author sontt1
 * Date:9/28/2022
 * Time:4:02 PM
 */
@Slf4j
public class RabbitChannelFactory extends BasePooledObjectFactory<Channel> {
    private Connection connection;

    public RabbitChannelFactory(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Channel create() throws Exception {
        Channel channel = connection.createChannel();
        return channel;
    }

    @Override
    public PooledObject<Channel> wrap(Channel t) {
        return new DefaultPooledObject<>(t);
    }

    @Override
    public boolean validateObject(PooledObject<Channel> p) {
        return p.getObject().isOpen();
    }

    @Override
    public void activateObject(PooledObject<Channel> p) {
        p.getObject().isOpen();
    }

    @Override
    public void destroyObject(PooledObject<Channel> p) throws Exception {
        try {
            p.getObject().close();
        } catch (IOException | TimeoutException ex) {
            log.error("DestroyObject has ex: ", ex);
        }
    }

}
