package com.zemlyak.web.processing;

import org.springframework.messaging.Message;

import java.util.concurrent.atomic.AtomicInteger;

public class DelaySimulateEndpoint {
    private AtomicInteger counter = new AtomicInteger();

    public void process(Message<?> message){
        try {
            int counterVal = counter.incrementAndGet();
            System.out.println("Thread '" + Thread.currentThread().getName() + "' in DelaySimulateEndpoint. " + counterVal);
            Thread.sleep(1000);
            System.out.println("Thread '" + Thread.currentThread().getName() + "' in DelaySimulateEndpoint. End execution " + counterVal);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
