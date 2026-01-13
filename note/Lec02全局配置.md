# 全局配置

* RPC 框架存在很多配置信息，比如注册中心的地址、序列化方式、网络服务器端口号等
* 需要编写配置文件实现自定义配置

## 设计方案

### 配置项

* 基础配置
  * name 名称
  * version 版本号
  * serverHost 服务器主机名
  * serverPort 服务器端口号
* 扩展配置
  * 注册中心地址
  * 服务接口
  * 序列化方式
  * 网络通信协议
  * 超时设置
  * 负载均衡策略
  * 服务端线程模型

### 读取配置文件

* 使用 Hutool 的 Setting 模块读取 properties 文件

## 开发实现

* 配置加载
  * 新建配置类 `RpcConfig`，保存配置信息
  * 新建工具类 `ConfigUtils`，读取配置文件并返回配置对象，简化配置
  * 新建 `RpcConstant`接口，存储 RPC 相关常量
* 维护全局配置对象
  * 新建启动类 `RpcApplication`，使用双锁单例模式，作为项目启动入口，维护全局变量
  * 新建 `application.properties`，存储配置
* 创建示例消费者测试配置文件读取
* 创建实例提供者测试动态启动RPC服务

## 扩展

* 支持读取 `application.yml`，`application.yaml`等不同配置文件
* 支持监听文件变更并自动更新
* 支持中文
* 配置分组（dev、prod等）
