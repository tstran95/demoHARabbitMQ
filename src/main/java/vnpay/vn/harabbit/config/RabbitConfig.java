//package vnpay.vn.harabbit.config;
//
//import com.rabbitmq.client.ConnectionFactory;
//import org.springframework.amqp.core.Binding;
//import org.springframework.amqp.core.BindingBuilder;
//import org.springframework.amqp.core.DirectExchange;
//import org.springframework.amqp.core.Queue;
//import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
//import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import vnpay.vn.harabbit.constant.Constant;
//
///**
// * @author sontt1
// * Date:9/28/2022
// * Time:9:55 AM
// */
//@Configuration
//public class RabbitConfig {
//    static final String topicExchangeName = Constant.EXCHANGE;
//
//    static final String queueName = Constant.QUEUE;
//
//    @Bean
//    Queue queue() {
//        return new Queue(queueName, false);
//    }
//
//    @Bean
//    DirectExchange exchange() {
//        return new DirectExchange(topicExchangeName);
//    }
//
//    @Bean
//    Binding binding(Queue queue, DirectExchange exchange) {
//        return BindingBuilder.bind(queue).to(exchange).with(Constant.ROUTING_KEY);
//    }
//
//    @Bean
//    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
//                                             MessageListenerAdapter listenerAdapter) {
//        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
//        container.setConnectionFactory(connectionFactory);
//        container.setQueueNames(queueName);
//        container.setMessageListener(listenerAdapter);
//        return container;
//    }
//
//}
