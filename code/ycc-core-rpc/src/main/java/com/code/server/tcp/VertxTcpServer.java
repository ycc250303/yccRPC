package com.code.server.tcp;

import com.code.server.http.HttpServer;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetServer;
import io.vertx.core.parsetools.RecordParser;

public class VertxTcpServer implements HttpServer {
    private byte[] handleRequest(byte[] requestData) {
        return "Hello,client!".getBytes();
    }

    @Override
    public void doStart(int port) {
        Vertx vertx = Vertx.vertx();

        // 创建 TCP 服务器
        NetServer server = vertx.createNetServer();

        server.connectHandler(socket -> {
            // 1. 构造一个记录解析器，初始状态只读取 8 字节（协议头长度）
            RecordParser parser = RecordParser.newFixed(8);

            parser.setOutput(new Handler<Buffer>() {
                // 标记当前是否正在读取 Body
                int bodySize = -1;

                @Override
                public void handle(Buffer buffer) {
                    if (bodySize == -1) {
                        // 情况 A：当前读取的是 8 字节协议头
                        // 假设：第 4-7 字节是 int 类型的 body 长度
                        bodySize = buffer.getInt(4);
                        // 切换解析器模式：下次读取 bodySize 长度的字节
                        parser.fixedSizeMode(bodySize);
                    } else {
                        // 情况 B：当前读取的是 Body 数据
                        // 此时 buffer 就是完整的 Body 内容，无需从 resultBuffer 截取
                        byte[] bodyBytes = buffer.getBytes();
                        System.out.println("Received request body: " + new String(bodyBytes));

                        // 3. 处理业务逻辑并返回响应
                        byte[] responseData = handleRequest(bodyBytes);

                        // 构造响应包：Header(8字节) + Body
                        Buffer responseBuffer = Buffer.buffer();
                        responseBuffer.appendInt(0); // 协议头占位/魔数等
                        responseBuffer.appendInt(responseData.length); // 写入 Body 长度
                        responseBuffer.appendBytes(responseData); // 写入 Body 内容

                        socket.write(responseBuffer);

                        // 4. 重置状态：切换解析器模式回到 8 字节，准备读取下一个请求的 Header
                        parser.fixedSizeMode(8);
                        bodySize = -1;
                    }
                }
            });

            socket.handler(parser);
        });

        server.listen(port, result -> {
            if (result.succeeded()) {
                System.out.println("TCP server is now listening on port " + port);
            } else {
                System.out.println("Failed to start TCP server: " + result.cause());
            }
        });
    }

    public static void main(String[] args) {
        new VertxTcpServer().doStart(8080);
    }
}
