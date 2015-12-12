package com.zemlyak.web.processing;

import org.springframework.messaging.Message;

public interface HeaderPrinter {
    Message<?> print(Message<?> message);
}
