package com.zemlyak.web.processing;

import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;

import java.util.Map;

public class ToResponseTransformer implements MessageTransformer<String,SimpleResponse> {
    @Override
    public Message<SimpleResponse> transform(Message<String> message) {
        System.out.println("Message: " + message.getPayload());
        for(Map.Entry<String, Object> headerEntry : message.getHeaders().entrySet()){
            System.out.println("header '" + headerEntry.getKey() + "': " + headerEntry.getValue());
        }

        /*if(true){
            throw new IllegalStateException("Some illegal state");
        }    */

        Message<SimpleResponse> targetMessage = MessageBuilder
                .withPayload(new SimpleResponse())
                .copyHeaders( message.getHeaders())
                .setHeader("custom-header", "custom-value")
                .build();
        return targetMessage;
    }
}
