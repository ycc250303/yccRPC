package com.code.config;

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
}
