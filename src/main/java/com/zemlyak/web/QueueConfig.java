package com.zemlyak.web;

import com.zemlyak.web.processing.DelaySimulateEndpoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.scheduling.PollerMetadata;
import org.springframework.messaging.MessageChannel;
import org.springframework.scheduling.support.PeriodicTrigger;

import java.util.concurrent.Executor;

@EnableIntegration
@Configuration
public class QueueConfig {
    @Bean
    public MessageChannel queChannel(){
        return new QueueChannel(5);
    }

    @Bean
    @ServiceActivator(inputChannel = "queChannel")
    public DelaySimulateEndpoint delaySimulateEndpoint(){
        return new DelaySimulateEndpoint();
    }

    @Bean(name = PollerMetadata.DEFAULT_POLLER)
    public PollerMetadata defaultPoller() {
        PollerMetadata pollerMetadata = new PollerMetadata();
        pollerMetadata.setTrigger(new PeriodicTrigger(500));
        return pollerMetadata;
    }
}
