package com.code.spi;

import com.code.serializer.Serializer;

import java.util.Map;

public class SpiLoaderTest {
    public static void main(String[] args) {
        // 1. 测试加载
        System.out.println("--- 开始加载 SPI ---");
        Map<String, Class<?>> keyClassMap = SpiLoader.load(Serializer.class);
        System.out.println("加载结果: " + keyClassMap);

        // 2. 测试获取实例
        System.out.println("\n--- 获取指定实例 ---");
        Serializer jsonSerializer = SpiLoader.getInstance(Serializer.class, "json");
        System.out.println("获取到 Json 序列化器: " + jsonSerializer.getClass().getName());

        Serializer jdkSerializer = SpiLoader.getInstance(Serializer.class, "jdk");
        System.out.println("获取到 Jdk 序列化器: " + jdkSerializer.getClass().getName());
    }
}
