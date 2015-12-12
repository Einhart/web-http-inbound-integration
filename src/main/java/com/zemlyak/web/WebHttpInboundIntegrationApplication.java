package com.zemlyak.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.http.inbound.HttpRequestHandlingMessagingGateway;
import org.springframework.integration.http.inbound.RequestMapping;
import org.springframework.messaging.MessageChannel;

import java.util.ArrayList;
import java.util.List;

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
    public HttpRequestHandlingMessagingGateway httpInbound(){
        RequestMapping mapping = new RequestMapping();
        mapping.setPathPatterns("/rest/service/test");
        mapping.setMethods(HttpMethod.POST);

        HttpRequestHandlingMessagingGateway gateway = new HttpRequestHandlingMessagingGateway();
        gateway.setReplyChannel(httpReplyChannel());
        gateway.setRequestChannel(httpRequestChannel());

        gateway.setMessageConverters(getConverters());
        gateway.setRequestMapping(mapping);
        gateway.setRequestPayloadType(String.class);
        return gateway;
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
