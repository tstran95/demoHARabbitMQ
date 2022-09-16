package vnpay.vn.harabbit.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import vnpay.vn.harabbit.service.AppService;

/**
 * @author sontt1
 * Date:9/14/2022
 * Time:5:10 PM
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AppServiceImpl implements AppService {
    private final RabbitTemplate rabbit;
    private final static String EXCHANGE = "custom.myexchange1";

    @Override
    public void sendMessage(String message) {
        log.info("Method sendMessage() START with message {}", message);
        try {
            rabbit.convertAndSend(
                    EXCHANGE,
                    "doesntmatter"
                    , message);
        }catch (Exception e) {
            throw new RuntimeException("Cant connect to RabbitMQ server!!!!");
        }
        log.info("Method sendMessage() END");
    }
}
