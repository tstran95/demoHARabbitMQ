package vnpay.vn.harabbit.core;

import com.rabbitmq.client.Address;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import vnpay.vn.harabbit.bean.RabbitMQBean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * @author sontt1
 * Date:9/28/2022
 * Time:4:04 PM
 */

@Slf4j
public class RabbitMQ {
    protected Connection connection;

    private ConnectionFactory factory;

    @Getter
    @Setter
    private RabbitMQBean rabbitMqBean;

    private static final class SingletonHolder {

        private static final RabbitMQ INSTANCE = new RabbitMQ();
    }

    public static RabbitMQ getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * @return
     */
    public boolean openConnection() {
        try {
            Address[] address = {new Address("172.19.0.5" , 5_000) ,
                                new Address("172.19.0.3" , 5_000),
                                new Address("172.19.0.2" , 5_000)};
            factory = new ConnectionFactory();
            factory.setUsername("sontt");
            factory.setPassword("sontt");
            factory.setAutomaticRecoveryEnabled(true);
            factory.setRequestedHeartbeat(45);
            factory.setConnectionTimeout(60000);
            connection = factory.newConnection(address);
            log.info("Connect success to RabbitMQ ");
            return true;
        } catch (Exception e) {
            log.error("Connection to RabbitMQ have ex", e);
            return false;
        }
    }

    /**
     * @return
     */
    private Address[] genAddress() {
        String[] hostPorts = rabbitMqBean.getHostPort().split("/");
        List<Address> listAddress = new ArrayList();
        String[] strHostPort;
        String host;
        String port;

        for (String hostPort : hostPorts) {
            strHostPort = hostPort.split(":");
            host = strHostPort[0];
            port = strHostPort[1];
            final Address address = new Address(host, Integer.parseInt(port));
            listAddress.add(address);
        }

        Address[] arrAddress = new Address[]{};
        return listAddress.toArray(arrAddress);
    }

    /**
     * @return
     */
    public Connection getConnection() {
        if (null == connection || !connection.isOpen()) {
            openConnection();
        }
        return connection;
    }

    /**
     * Closes the Queue Connection. This is not needed to be called explicitly
     * as connection closure happens implicitly anyways.
     */
    public void close() {
        try {
            factory.setAutomaticRecoveryEnabled(false);
            if (connection != null) {
                connection.close();
            }
            connection = null;
        } catch (IOException ex) {
            log.error("Error close connection to MQ Server...", ex);
        }
    }
}
