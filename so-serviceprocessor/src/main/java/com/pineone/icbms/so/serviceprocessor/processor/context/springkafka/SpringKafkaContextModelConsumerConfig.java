package com.pineone.icbms.so.serviceprocessor.processor.context.springkafka;

import com.pineone.icbms.so.util.spring.springkafka.consumer.AConsumerConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;

/**
 * support object for KafkaConsumer.<BR/>
 *
 * Created by uni4love on 2017. 4. 10..
 */
@Configuration
@ConditionalOnProperty(prefix = "so.serviceprocessor.contextmodel", name = "enable", havingValue = "true")
@EnableKafka
public class SpringKafkaContextModelConsumerConfig extends AConsumerConfig {
    /**
     * kafka handler group id by class name.<BR/>
     */
    private final String GROUP_ID = getClass().getSimpleName();

    /**
     * return group id.<BR/>
     *
     * @return group id
     */
    @Override
    public String getGroupId() {
        return GROUP_ID;
    }

    /**
     * return listener class.<BR/>
     *
     * @return SpringKafkaContextModelConsumerHandler
     */
    @Bean
    public SpringKafkaContextModelConsumerHandler contextModelConsumerHandler() {
        return new SpringKafkaContextModelConsumerHandler();
    }
}
