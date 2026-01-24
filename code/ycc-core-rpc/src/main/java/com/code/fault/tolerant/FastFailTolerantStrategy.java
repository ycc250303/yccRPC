package com.code.fault.tolerant;

import com.code.model.RpcResponse;

import java.util.Map;

public class FastFailTolerantStrategy implements TolerantStrategy{

    @Override
    public RpcResponse doTolerant(Map<String, Object> context, Exception e) {
        throw new RuntimeException("服务出错",e);
    }
}
