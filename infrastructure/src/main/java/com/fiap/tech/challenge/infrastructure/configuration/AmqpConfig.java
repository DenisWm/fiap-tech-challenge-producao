package com.fiap.tech.challenge.infrastructure.configuration;

import com.fiap.tech.challenge.infrastructure.configuration.annotations.ProductionEvents;
import com.fiap.tech.challenge.infrastructure.configuration.annotations.ProductionStatusChangedQueue;
import com.fiap.tech.challenge.infrastructure.configuration.properties.amqp.QueueProperties;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmqpConfig {

    @Bean
    @ConfigurationProperties("amqp.queues.production-status-changed")
    @ProductionStatusChangedQueue
    public QueueProperties productionInPreparationQueueProperties() {
        return new QueueProperties();
    }


    @Configuration
    static class Admin {

        @Bean
        @ProductionEvents
        public DirectExchange productionEventsExchange(@ProductionStatusChangedQueue QueueProperties props) {
            return new DirectExchange(props.getExchange());
        }

        @Bean
        @ProductionStatusChangedQueue
        public Queue productionStatusChangedQueue(@ProductionStatusChangedQueue QueueProperties props) {
            return new Queue(props.getQueue());
        }

        @Bean
        @ProductionStatusChangedQueue
        public Binding productionStatusChangedBinding(
                @ProductionEvents DirectExchange exchange,
                @ProductionStatusChangedQueue Queue queue,
                @ProductionStatusChangedQueue QueueProperties props
        ) {
            return BindingBuilder.bind(queue).to(exchange).with(props.getRoutingKey());
        }
    }
}
