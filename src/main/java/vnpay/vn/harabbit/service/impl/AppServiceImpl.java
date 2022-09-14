package vnpay.vn.harabbit.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import vnpay.vn.harabbit.service.AppService;

/**
 * @author sontt1
 * Date:9/14/2022
 * Time:5:10 PM
 */
@Service
@RequiredArgsConstructor
public class AppServiceImpl implements AppService {
    private final RabbitTemplate rabbit;
    private final static String EXCHANGE = "custom.myexchange1";

    @Override
    public void sendMessage(String message) {
        rabbit.convertAndSend(
                EXCHANGE,
                "doesntmatter"
                , message);
    }
}
