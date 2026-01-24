package com.code.config;

import com.code.fault.retry.RetryStrategyKeys;
import com.code.fault.tolerant.TolerantStrategyKeys;
import com.code.loadbalancer.LoadBalancer;
import com.code.loadbalancer.LoadBalancerKeys;
import com.code.serializer.SerializerKeys;
import lombok.Data;

@Data
public class RpcConfig {
    /**
     * 服务名称
     */
    private String name = "ycc-rpc";

    /**
     * 服务版本
     */
    private String version = "1.0";

    /**
     * 服务地址
     */
    private String serverHost = "localhost";

    /**
     * 服务端口
     */
    private int serverPort = 8080;

    /**
     * 是否开启mock
     */
    private boolean mock = false;

    /**
     * 序列化方式
     */
    private String serializer = SerializerKeys.JSON;

    /**
     * 注册中心配置
     */
    private RegistryConfig registryConfig = new RegistryConfig();

    /**
     * 负载均衡方式
     */
    private String loadBalancer = LoadBalancerKeys.ROUND_ROBIN;

    /**
     * 重试策略
     */
    private String retryStrategy = RetryStrategyKeys.NO;

    /**
     * 容错策略
     */
    private String tolerantStrategy = TolerantStrategyKeys.FAIL_FAST;
}
