package vnpay.vn.harabbit.core;

import com.rabbitmq.client.Connection;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Slf4j
public class ConnectionFact {
    private static Connection connection;

    private ConnectionFact() {
        super();
    }

    public static Connection getInstance() throws IOException, TimeoutException {
        if (connection == null) {
            connection = createConnection();
        }
        return connection;
    }

    public static Connection createConnection() throws IOException, TimeoutException {
        log.info("ConnectionManager method createConnection() START");
        com.rabbitmq.client.ConnectionFactory factory = new com.rabbitmq.client.ConnectionFactory();
        factory.setHost("localhost");
        factory.setPort(5000);
        factory.setUsername("sontt");
        factory.setPassword("sontt");
        log.info("ConnectionManager method createConnection() END");
        return factory.newConnection();
    }
}
