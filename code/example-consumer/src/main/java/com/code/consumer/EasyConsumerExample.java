package com.code.consumer;

import com.code.model.User;
import com.code.proxy.ServiceProxyFactory;
import com.code.service.UserService;

public class EasyConsumerExample {
    public static void main(String[] args) {
        // TODO: 需要获取 UserService的实现类对象
        UserService userService = ServiceProxyFactory.getProxy(UserService.class);
        User user = new User();
        user.setName("ycc");

        // 调用
        User NewUser = userService.getUser(user);
        if (NewUser != null) {
            System.out.println(NewUser.getName());
        } else {
            System.out.println("没有该用户");
        }
    }
}
