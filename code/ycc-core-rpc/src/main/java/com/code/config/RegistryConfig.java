package com.code.config;

import lombok.Data;

@Data
public class RegistryConfig {
    /**
     * 注册中心类别
     */
    private String registry = "etcd";

    /**
     * 注册地址
     */
    private String address = "http://localhost:2379";

    /**
     * 注册用户名
     */
    private String username;

    /**
     * 注册密码
     */
    private String password;

    /**
     * 注册超时时间（毫秒单位）
     */
    private Long timeout = 10000L;
}
