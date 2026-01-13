package com.code.consumer;

import com.code.config.RpcConfig;
import com.code.utils.ConfigUtils;

/**
 * 简易服务消费者示例
 */
public class ConsumerExample {

    public static void main(String[] args) {
        RpcConfig rpc = ConfigUtils.loadConfig(RpcConfig.class, "rpc");
        System.out.println(rpc);
    }
}
