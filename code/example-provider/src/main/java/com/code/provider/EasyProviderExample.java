package com.code.provider;

import com.code.registry.LocalRegistry;
import com.code.server.http.HttpServer;
import com.code.server.http.VertxHttpServer;
import com.code.service.UserService;

public class EasyProviderExample {
    public static void main(String[] args) {
        // 注册服务
        LocalRegistry.register(UserService.class.getName(), UserServiceImpl.class);

        // 启动 web 服务
        HttpServer httpServer = new VertxHttpServer();
        httpServer.doStart(8080);
    }
}

