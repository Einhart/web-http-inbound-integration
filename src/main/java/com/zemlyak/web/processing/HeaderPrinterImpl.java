package com.zemlyak.web.processing;

import org.springframework.messaging.Message;

import java.util.Map;

public class HeaderPrinterImpl implements HeaderPrinter {
    @Override
    public Message<?> print(Message<?> message) {
        for(Map.Entry<String, Object> headerEntry : message.getHeaders().entrySet()){
            System.out.println("header '" + headerEntry.getKey() + "': " + headerEntry.getValue());
        }
        return message;
    }
}
