package com.code.server.tcp;

import cn.hutool.core.util.IdUtil;
import com.code.RpcApplication;
import com.code.model.RpcRequest;
import com.code.model.RpcResponse;
import com.code.model.ServiceMetaInfo;
import com.code.protocol.*;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetSocket;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class VertxTcpClient {
    public void start(int port) {
        Vertx vertx = Vertx.vertx();

        vertx.createNetClient().connect(port, "localhost", result -> {
            if (result.succeeded()) {
                System.out.println("Connected to TCP server");
                io.vertx.core.net.NetSocket socket = result.result();

                for (int i = 0; i < 100; i++) {
                    Buffer buffer = Buffer.buffer();
                    String str = "Hello, server!";
                    buffer.appendInt(0);
                    buffer.appendInt(str.getBytes().length);
                    buffer.appendString(str);
                    socket.write(buffer);
                }

                socket.handler(buffer -> {
                    System.out.println("Received response from server: " + buffer.toString());
                });
            } else {
                System.err.println("Failed to connect to TCP server");
            }
        });
    }

    /**
     * 发送请求
     *
     * @param rpcRequest
     * @param serviceMetaInfo
     * @return
     * @throws InterruptedException
     * @throws ExecutionException
     */
    public static RpcResponse doRequest(RpcRequest rpcRequest, ServiceMetaInfo serviceMetaInfo)
            throws InterruptedException, ExecutionException {
        // 发送 TCP 请求
        Vertx vertx = Vertx.vertx();
        NetClient netClient = vertx.createNetClient();
        CompletableFuture<RpcResponse> responseFuture = new CompletableFuture<>();
        netClient.connect(serviceMetaInfo.getServicePort(), serviceMetaInfo.getServiceHost(),
                result -> {
                    if (!result.succeeded()) {
                        System.err.println("Failed to connect to TCP server: " + result.cause().getMessage());
                        // 完成 future 并设置异常，以便重试机制能够捕获
                        responseFuture.completeExceptionally(
                                new RuntimeException("连接TCP服务器失败: " + result.cause().getMessage(), result.cause()));
                        netClient.close();
                        return;
                    }
                    NetSocket socket = result.result();
                    // 发送数据
                    // 构造消息
                    ProtocolMessage<RpcRequest> protocolMessage = new ProtocolMessage<>();
                    ProtocolMessage.Header header = new ProtocolMessage.Header();
                    header.setMagic(ProtocolConstant.PROTOCOL_MAGIC);
                    header.setVersion(ProtocolConstant.PROTOCOL_VERSION);
                    header.setSerializer((byte) ProtocolMessageSerializerEnum
                            .getEnumByValue(RpcApplication.getRpcConfig().getSerializer()).getKey());
                    header.setType((byte) ProtocolMessageTypeEnum.REQUEST.getKey());
                    // 生成全局请求 ID
                    header.setRequestId(IdUtil.getSnowflakeNextId());
                    protocolMessage.setHeader(header);
                    protocolMessage.setBody(rpcRequest);

                    // 编码请求
                    try {
                        Buffer encodeBuffer = ProtocolMessageEncoder.encode(protocolMessage);
                        socket.write(encodeBuffer);
                    } catch (IOException e) {
                        responseFuture.completeExceptionally(new RuntimeException("协议消息编码错误", e));
                        netClient.close();
                        return;
                    }

                    // 接收响应
                    TcpBufferHandlerWrapper bufferHandlerWrapper = new TcpBufferHandlerWrapper(
                            buffer -> {
                                try {
                                    ProtocolMessage<RpcResponse> rpcResponseProtocolMessage = (ProtocolMessage<RpcResponse>) ProtocolMessageDecoder
                                            .decode(buffer);
                                    responseFuture.complete(rpcResponseProtocolMessage.getBody());
                                } catch (IOException e) {
                                    responseFuture.completeExceptionally(new RuntimeException("协议消息解码错误", e));
                                    netClient.close();
                                }
                            });
                    socket.handler(bufferHandlerWrapper);

                    // 处理连接异常关闭的情况
                    socket.exceptionHandler(throwable -> {
                        responseFuture.completeExceptionally(new RuntimeException("TCP连接异常", throwable));
                        netClient.close();
                    });

                    socket.closeHandler(v -> {
                        // 如果连接关闭但 future 还未完成，说明可能是异常关闭
                        if (!responseFuture.isDone()) {
                            responseFuture.completeExceptionally(new RuntimeException("TCP连接已关闭"));
                            netClient.close();
                        }
                    });

                });

        RpcResponse rpcResponse = responseFuture.get();
        // 记得关闭连接
        netClient.close();
        return rpcResponse;
    }

    public static void main(String[] args) {
        new VertxTcpClient().start(8080);
    }
}
