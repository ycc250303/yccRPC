package com.code.protocol;

import java.io.IOException;

import com.code.model.RpcRequest;
import com.code.model.RpcResponse;
import com.code.serializer.Serializer;
import com.code.serializer.SerializerFactory;
import io.vertx.core.buffer.Buffer;

public class ProtocolMessageDecoder {
    public static ProtocolMessage<?> decode(Buffer buffer) throws IOException {
        ProtocolMessage.Header header = new ProtocolMessage.Header();
        byte magic = buffer.getByte(0);
        if (magic != ProtocolConstant.PROTOCOL_MAGIC) {
            throw new IOException("magic not match");
        }
        header.setMagic(magic);
        header.setVersion(buffer.getByte(1));
        header.setSerializer(buffer.getByte(2));
        header.setType(buffer.getByte(3));
        header.setStatus(buffer.getByte(4));
        header.setRequestId(buffer.getLong(5));
        header.setBodyLength(buffer.getInt(13));

        byte[] bodeBytes = buffer.getBytes(ProtocolConstant.MESSAGE_HEADER_LENGTH, ProtocolConstant.MESSAGE_HEADER_LENGTH + header.getBodyLength());

        ProtocolMessageSerializerEnum serializerEnum = ProtocolMessageSerializerEnum.getEnumByKey(header.getSerializer());
        if (serializerEnum == null) {
            throw new IOException("serializer not found");
        }
        Serializer serializer = SerializerFactory.getInstance(serializerEnum.getValue());

        ProtocolMessageTypeEnum messageTypeEnum = ProtocolMessageTypeEnum.getEnumByKey(header.getType());
        if (messageTypeEnum == null) {
            throw new IOException("message type not found");
        }

        switch (messageTypeEnum) {
            case REQUEST:
                RpcRequest rpcRequest = serializer.deserialize(bodeBytes, RpcRequest.class);
                return new ProtocolMessage<>(header, rpcRequest);
            case RESPONSE:
                RpcResponse response = serializer.deserialize(bodeBytes, RpcResponse.class);
                return new ProtocolMessage<>(header, response);
            case HEART_BEAT:
            case OTHERS:
            default:
                throw new RuntimeException("message type not support");
        }

    }
}
