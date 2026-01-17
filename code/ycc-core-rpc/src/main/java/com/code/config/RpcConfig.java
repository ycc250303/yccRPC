package com.code.config;

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
}
