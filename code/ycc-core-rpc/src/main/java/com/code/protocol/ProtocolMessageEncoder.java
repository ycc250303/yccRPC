package com.code.protocol;

import java.io.IOException;

import com.code.serializer.Serializer;
import com.code.serializer.SerializerFactory;
import io.vertx.core.buffer.Buffer;

public class ProtocolMessageEncoder {

    public static Buffer encode(ProtocolMessage<?> protocolMessage) throws IOException{
        if(protocolMessage == null || protocolMessage.getHeader() == null){
            return Buffer.buffer();
        }
        ProtocolMessage.Header header = protocolMessage.getHeader();
        Buffer buffer = Buffer.buffer();
        buffer.appendByte(header.getMagic());
        buffer.appendByte(header.getVersion());
        buffer.appendByte(header.getSerializer());
        buffer.appendByte(header.getType());
        buffer.appendByte(header.getStatus());
        buffer.appendLong(header.getRequestId());
        // 获取序列化器
        ProtocolMessageSerializerEnum serializerEnum = ProtocolMessageSerializerEnum.getEnumByKey(header.getSerializer());
        if(serializerEnum == null){
            throw new IOException("serializer not found");
        }
        // 序列化
        Serializer serializer = SerializerFactory.getInstance(serializerEnum.getValue());
        byte[] bytes = serializer.serialize(protocolMessage.getBody());
        // 写入body长度
        buffer.appendInt(bytes.length);
        buffer.appendBytes(bytes);
        return buffer;
    }
}
