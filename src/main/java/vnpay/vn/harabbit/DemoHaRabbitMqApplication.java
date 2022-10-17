package vnpay.vn.harabbit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import vnpay.vn.harabbit.core.RabbitMQ;
import vnpay.vn.harabbit.core.RabbitMQPool;

@SpringBootApplication
public class DemoHaRabbitMqApplication {
    public static void main(String[] args) {
        RabbitMQ.getInstance().getConnection();
        RabbitMQPool.getInstance().start();
        SpringApplication.run(DemoHaRabbitMqApplication.class, args);
    }

}
