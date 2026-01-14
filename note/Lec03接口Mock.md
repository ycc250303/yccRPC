# 接口 Mock

* 实际开发/测试过程中，有时可能无法直接访问真实的远程服务，或者访问真实服务影响不可控
* 使用 mock 服务模拟远程服务，进行接口的开发、调试和测试

## 设计方案

* 通过动态代理创建一个远程调用方法时返回固定值的对象

## 开发实现

* 给全局配置类 `RpcConfig`新增mock字段
* 新增 `MockServiceProxy`类，用于生成 mock 代理服务，提供一个根据服务接口类型返回固定值的方法
* 给 `ServiceProxyFactory`服务代理工厂新增获取mock代理对象的方法 `getMockProxy`
* 在 `exmaple-common`模块新增一个默认实现返回数字1的新方法
* 在消费者配置文件中将mock设置为 true
*
