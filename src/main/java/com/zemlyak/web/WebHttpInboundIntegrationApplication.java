package com.zemlyak.web;

import com.zemlyak.web.processing.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.http.inbound.HttpRequestHandlingMessagingGateway;
import org.springframework.integration.http.inbound.RequestMapping;
import org.springframework.messaging.MessageChannel;

import java.util.*;

@Configuration
@SpringBootApplication
public class WebHttpInboundIntegrationApplication {
    @Bean
    public MessageChannel httpReplyChannel(){
        return new DirectChannel();
    }

    @Bean
    public MessageChannel httpRequestChannel(){
        return new DirectChannel();
    }

    @Bean
    public MessageChannel checkHeaders(){
        return new DirectChannel();
    }

    @Bean
    public MessageChannel requestErrorChannel(){
        return new DirectChannel();
    }

    @Bean
    public HttpRequestHandlingMessagingGateway httpInbound(){
        RequestMapping mapping = new RequestMapping();
        mapping.setPathPatterns("/rest/service/test");
        mapping.setMethods(HttpMethod.POST);

        HttpRequestHandlingMessagingGateway gateway = new HttpRequestHandlingMessagingGateway();
        gateway.setReplyChannel(httpReplyChannel());
        gateway.setRequestChannel(httpRequestChannel());
        gateway.setErrorChannel(requestErrorChannel());
        gateway.setMessageConverters(getConverters());
        gateway.setRequestMapping(mapping);
        gateway.setRequestPayloadType(String.class);
        gateway.setMergeWithDefaultConverters(true);
        return gateway;
    }

    @Bean
    @Transformer(inputChannel="httpRequestChannel",outputChannel = "checkHeaders")
    public MessageTransformer<String,SimpleResponse> requestTransformer(){
        return new ToResponseTransformer();
    }

    @Bean
    @ServiceActivator(inputChannel="checkHeaders",outputChannel = "httpReplyChannel")
    public HeaderPrinter headerPrinter(){
        return new HeaderPrinterImpl();
    }

    @Bean
    @ServiceActivator(inputChannel="requestErrorChannel",outputChannel = "httpReplyChannel")
    public HeaderPrinter headerPrinter2(){
        return new HeaderPrinterImpl();
    }

    private List<HttpMessageConverter<?>> getConverters(){
        List<HttpMessageConverter<?>> converters=new ArrayList<>();
        converters.add(new StringHttpMessageConverter());
        converters.add(new MappingJackson2HttpMessageConverter());
        return converters;
    }

    public static void main(String[] args) {
        SpringApplication.run(WebHttpInboundIntegrationApplication.class, args);
    }
}
