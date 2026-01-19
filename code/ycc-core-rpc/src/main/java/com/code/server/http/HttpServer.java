package com.code.server.http;

public interface HttpServer {
    /**
     * 启动服务
     * @param port
     */
    void doStart(int port);
}
