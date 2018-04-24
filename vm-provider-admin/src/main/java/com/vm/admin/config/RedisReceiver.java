package com.vm.admin.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class RedisReceiver {
    final static Logger logger = LoggerFactory.getLogger(RedisReceiver.class);
    public void receiveMessage(Object message) {
        //这里是收到通道的消息之后执行的方法
        logger.info("RedisReceiver receiveMessage is : {} ! ",message);
    }
}