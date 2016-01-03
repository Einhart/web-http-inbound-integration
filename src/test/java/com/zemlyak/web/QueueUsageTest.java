package com.zemlyak.web;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = QueueConfig.class)
//@WebAppConfiguration
public class QueueUsageTest {
    @Autowired
    private MessageChannel queChannel;

    @Test
    public void contextLoads() {
        final AtomicInteger counter = new AtomicInteger();
        Runnable run = new Runnable() {
            @Override
            public void run() {
                int countVal = counter.incrementAndGet();
                System.out.println("Thread '" + countVal + "' start");
                Message<String> message = MessageBuilder.withPayload("qwer").build();
                queChannel.send(message);
                System.out.println("Thread '" + countVal + "' end");
            }
        };

        List<Thread> threadList = new ArrayList<>(20);
        for(int i = 0; i < 20; i++){
            Thread thread = new Thread(run);
            thread.setName("To queue sender " + i);
            threadList.add(thread);
        }

        for(Thread thread: threadList){
            thread.start();
        }
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
