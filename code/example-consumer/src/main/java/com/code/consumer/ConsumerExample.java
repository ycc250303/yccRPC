package com.code.consumer;

import com.code.config.RpcConfig;
import com.code.model.User;
import com.code.proxy.ServiceProxyFactory;
import com.code.service.UserService;
import com.code.utils.ConfigUtils;

/**
 * 简易服务消费者示例
 */
public class ConsumerExample {

    public static void main(String[] args) {
        System.out.println("================ 1 =============");
        // 获取代理
        UserService userService = ServiceProxyFactory.getProxy(UserService.class);
        User user = new User();
        user.setName("ycc");

        System.out.println("================ 2 =============");
        // 调用
        User newUser = userService.getUser(user);
        if (newUser != null) {
            System.out.println(newUser.getName());
        } else {
            System.out.println("user == null");
        }

        System.out.println("================ 3 =============");
        long number = userService.getNumber();
        System.out.println(number);
        System.out.println("================ 4 ============");
    }
}

