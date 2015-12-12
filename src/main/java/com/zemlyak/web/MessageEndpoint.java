package com.zemlyak.web;

import com.zemlyak.web.processing.SimpleResponse;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class MessageEndpoint {
    @Transformer(inputChannel="httpRequestChannel",outputChannel = "checkHeaders")
    public Message<SimpleResponse> handleMessage(Message<String> message) {
        System.out.println("Message: " + message.getPayload());
        for(Map.Entry<String, Object> headerEntry : message.getHeaders().entrySet()){
            System.out.println("header '" + headerEntry.getKey() + "': " + headerEntry.getValue());
        }

        Message<SimpleResponse> targetMessage = MessageBuilder
                .withPayload(new SimpleResponse())
                .copyHeaders( message.getHeaders())
                .setHeader("custom-header", "custom-value")
                .build();
        return targetMessage;
    }

    @ServiceActivator(inputChannel="checkHeaders",outputChannel = "httpReplyChannel")
    public Message<?> checkHeaders(Message<String> message) {
        for(Map.Entry<String, Object> headerEntry : message.getHeaders().entrySet()){
            System.out.println("header '" + headerEntry.getKey() + "': " + headerEntry.getValue());
        }
        return message;
    }
}
