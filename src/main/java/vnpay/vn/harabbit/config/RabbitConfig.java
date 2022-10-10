package vnpay.vn.harabbit.config;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vnpay.vn.harabbit.constant.Constant;

import java.util.HashMap;
import java.util.Map;

/**
 * @author sontt1
 * Date:9/28/2022
 * Time:9:55 AM
 */
@Configuration
public class RabbitConfig {
    @Bean
    Queue queue() {
        Map<String, Object> args = new HashMap<>();
//        args.put("x-queue-type", "quorum");
//        args.put("x-ha-policy" ,"all");
        return new Queue(Constant.QUEUE, true , false , false , null);
    }

    @Bean
    DirectExchange exchange() {
        return new DirectExchange(Constant.EXCHANGE);
    }

    @Bean
    Binding binding(Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(Constant.ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    public AmqpTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }
}
