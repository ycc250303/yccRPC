package com.code.server.http;

import io.vertx.core.Vertx;

public class VertxHttpServer implements HttpServer {

    public void doStart(int port) {
        // 创建vertx实例
        Vertx vertx = Vertx.vertx();

        // 创建http服务器
        io.vertx.core.http.HttpServer server = vertx.createHttpServer();

        // 监听端口并处理请求
        server.requestHandler(new HttpServerHandler());

        server.listen(port, result -> {
            if (result.succeeded()) {
                System.out.println("Server is now listening on port " + port);
            } else {
                System.out.println("Failed to start server: " + result.cause());
            }
        });
    }
}
