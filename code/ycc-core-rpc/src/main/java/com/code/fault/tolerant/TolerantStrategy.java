package com.code.fault.tolerant;

import com.code.model.RpcResponse;

import java.util.Map;

public interface TolerantStrategy {
    RpcResponse doTolerant(Map<String, Object> context,Exception e);
}
