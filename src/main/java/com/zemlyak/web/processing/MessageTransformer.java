package com.zemlyak.web.processing;

import org.springframework.messaging.Message;

public interface MessageTransformer<S,T> {
    Message<T> transform(Message<S> message);
}
