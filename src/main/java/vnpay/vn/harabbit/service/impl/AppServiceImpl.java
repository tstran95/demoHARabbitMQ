package vnpay.vn.harabbit.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import vnpay.vn.harabbit.service.AppService;
import vnpay.vn.harabbit.utils.Constant;

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

    @Override
    public void sendMessage(String message) {
        log.info("Method sendMessage() START with message {}", message);
        try {
            rabbit.convertAndSend(
                    Constant.EXCHANGE,
                    Constant.ROUTING_KEY,
                    message);
        } catch (Exception e) {
            log.error("Method sendMessage() ERROR with message ", e);
            throw new RuntimeException(Constant.CONNECT_RABBIT_ERROR);
        }
        log.info("Method sendMessage() END");
    }
}
