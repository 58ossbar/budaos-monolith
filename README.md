# BudaOS Monolith

<div align="center">

[![License](https://img.shields.io/badge/license-Apache%202.0-blue.svg)](LICENSE)
[![Java](https://img.shields.io/badge/Java-1.8+-green.svg)](https://adoptium.net/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.7.18-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![MySQL](https://img.shields.io/badge/MySQL-8.0+-orange.svg)](https://www.mysql.com/)

**BudaOS Monolith** 是布道师学习平台的单体架构开源版本，采用经典三层架构设计，为教育机构提供完整的企业级在线教学解决方案。

*100% 源代码开放 | 开箱即用 | 易于二次开发*

</div>

---

## 项目简介

BudaOS Monolith 是一个功能完备的在线学习管理平台（LMS），集成了教学管理、在线课堂、活动管理、社区互动、即时通讯等核心功能。系统采用前后端分离的架构设计，后端基于 Spring Boot 2.7 构建，支持快速部署和灵活扩展。

### 核心特性

- **模块化设计**：清晰的分层架构，易于维护和扩展
- **功能完备**：涵盖教学全流程，从课程管理到在线考试
- **社交学习**：内置社区论坛、即时通讯、学习圈
- **多端支持**：配套小程序、移动端 Web
- **安全可靠**：基于 Spring Security 的认证授权体系
- **实时交互**：WebSocket 支持实时课堂和消息推送

---

## 技术架构

### 技术栈

| 类别 | 技术选型 |
|------|----------|
| 基础框架 | Spring Boot 2.7.18 |
| 持久层 | MyBatis 2.3 + PageHelper 1.4 |
| 数据库 | MySQL 8.0 + Druid 连接池 |
| 缓存 | Redis (Session 存储) |
| 安全 | Spring Security + OAuth2 |
| 实时通信 | T-io WebSocket |
| 消息队列 | RabbitMQ / Redis MQ |
| 任务调度 | Quartz |
| API 文档 | Swagger 3.0 (SpringFox) |
| 微信生态 | weixin-popular SDK |
| 构建工具 | Maven |

### 项目结构

```
budaos-monolith/
├── budaos-parent/              # 父模块，统一定义依赖版本
│   └── pom.xml
├── budaos-common/              # 公共模块
│   └── src/main/java/          # 工具类、配置类、Domain实体
├── budaos-service/             # 服务模块
│   └── src/main/java/          # 业务服务实现、Mapper
├── budaos-web/                 # Web模块
│   └── src/main/java/          # REST控制器
├── budaos-application/         # 应用模块（启动入口）
│   └── src/main/resources/     # 配置文件
├── database/                   # 数据库脚本
│   └── db.sql
└── pom.xml                     # 项目根 POM
```

### 模块说明

| 模块 | 说明 |
|------|------|
| `budaos-parent` | 统一管理依赖版本、插件配置、公共属性 |
| `budaos-common` | 公共代码：工具类、枚举、常量、Domain实体、配置类 |
| `budaos-service` | 业务逻辑层：Service实现、Mapper接口、Mapper XML、定时任务 |
| `budaos-web` | 控制器层：REST API、拦截器、安全配置 |
| `budaos-application` | 应用打包模块，包含主启动类和配置 |

---

## 功能模块

### 教学管理
- **课堂管理**：创建/管理虚拟教室，设置课程表
- **活动管理**：支持签到、头脑风暴、投票问卷、问答讨论、课后作业、在线测试等多种教学活动
- **资源管理**：课程资源上传下载、云盘文件管理
- **成绩管理**：学生评分、作业批改

### 社区互动
- **博客论坛**：文章发布、评论互动、点赞收藏
- **好友系统**：关注好友、学习圈
- **即时通讯**：私信聊天、群组讨论（WebSocket 实时推送）

### 平台管理
- **用户管理**：教师/学生账号管理、第三方登录
- **微信集成**：微信登录、素材管理、消息推送
- **系统配置**：数据字典、站点设置、SEO优化

### 开发支持
- **API 文档**：Swagger UI 自动生成
- **日志管理**：统一日志记录、异常追踪
- **定时任务**：灵活的 Cron 表达式配置

---

## 快速开始

### 环境要求

| 软件 | 版本要求 | 说明 |
|------|----------|------|
| JDK | 1.8+ | 推荐 OpenJDK 11 |
| MySQL | 8.0+ | 支持 5.7+ |
| Redis | 3.0+ | 用于缓存和 Session |
| Maven | 3.0+ | 构建工具 |

### 部署步骤

#### 1. 准备数据库

```bash
# 登录 MySQL
mysql -u root -p

# 创建数据库
CREATE DATABASE budaos DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

# 导入数据脚本
USE budaos;
SOURCE database/db.sql;
```

#### 2. 配置 Redis

确保 Redis 服务已启动，默认配置：
- 端口：6379
- 密码：在 `application.yml` 中配置

#### 3. 修改配置文件

编辑 `budaos-application/src/main/resources/application.yml`：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/budaos?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai
    username: your_username
    password: your_password
  
  redis:
    host: your_redis_host
    password: your_redis_password
```

#### 4. 构建项目

```bash
# 进入项目目录
cd budaos-monolith

# Maven 打包（跳过测试）
mvn clean package -DskipTests
```

#### 5. 启动服务

```bash
# Windows 环境
.\start.bat

# Linux/Mac 环境
java -jar budaos-application/target/budaos-monolith.jar --spring.profiles.active=dev
```

#### 6. 访问系统

启动成功后访问：
- 访问地址：http://localhost:9080
- Swagger 文档：http://localhost:9080/swagger-ui/index.html

---

## 默认账号

| 角色 | 账号 | 密码 |
|------|------|------|
| 管理员 | admin | 888888 |
| 教师 | 15886345118 | 123456 |
| 学生 | 18229962587 | 123456 |

> ⚠️ **注意**：首次使用请及时修改默认密码

---

## 配置说明

### 核心配置项

```yaml
server:
  port: 9080                    # 服务端口

spring:
  datasource:
    url: jdbc:mysql://...        # 数据库连接
  redis:
    host: localhost              # Redis 主机
    port: 6379                   # Redis 端口

com.budaos:
  file-upload-path: /uploads    # 文件上传路径
  is-cors: true                  # 是否允许跨域
  captcha-enabled: true          # 验证码开关
```

### 微信配置

```yaml
# 第三方登录配置
com.budaos:
  wxAppId: your_wx_appid         # 微信 AppID
  wxAppKey: your_wx_appkey       # 微信 AppKey
```

---

## 开发指南

### 项目规范

1. **命名规范**
   - Service 接口：`XxxService.java`
   - Service 实现：`XxxServiceImpl.java`
   - Mapper 接口：`XxxMapper.java`
   - 控制器：`XxxController.java`

2. **分层结构**
   ```
   Controller  →  Service  →  Mapper  →  Database
   (处理请求)    (业务逻辑)   (数据访问)   (存储数据)
   ```

3. **返回值规范**
   - 使用统一返回类 `R`，包含 code、msg、data 字段
   - 成功：`R.ok()` 或 `R.ok(data)`
   - 失败：`R.error(msg)` 或 `R.error(code, msg)`

### API 开发示例

```java
@RestController
@RequestMapping("/api/example")
public class ExampleController {

    @Autowired
    private ExampleService exampleService;

    @GetMapping("/list")
    public R list(@RequestParam(defaultValue = "1") Integer pageNum,
                  @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Example> page = exampleService.getPage(pageNum, pageSize);
        return R.ok().put("data", page);
    }

    @PostMapping("/save")
    public R save(@RequestBody Example example) {
        exampleService.save(example);
        return R.ok("保存成功");
    }
}
```

### 添加新模块

1. 在 `budaos-common` 中添加 Domain 实体
2. 在 `budaos-service` 中添加 Mapper 和 Service
3. 在 `budaos-web` 中添加 Controller
4. 在 `database` 中添加对应的建表 SQL

---

## 常见问题

### Q: 启动报错 "Cannot connect to Redis"？
检查 Redis 服务是否启动，配置是否正确。

### Q: 文件上传失败？
1. 检查 `file-upload-path` 配置的目录是否存在
2. 确保目录有写入权限

### Q: 微信登录不生效？
配置微信开放平台的应用凭证（AppID 和 AppKey）

---

## 开源协议

本项目采用 [Apache License 2.0](LICENSE) 开源协议。

- 可免费用于个人和商业项目
- 需要保留版权声明
- 禁止直接使用本项目进行商业牟利（需获得授权）

---

## 参与贡献

欢迎提交 Issue 和 Pull Request！

1. Fork 本仓库
2. 创建分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add AmazingFeature'`)
4. 推送分支 (`git push origin feature/AmazingFeature`)
5. 创建 Pull Request

---

## 联系方式

- **官网**：https://www.budaos.com
- **邮箱**：contact@budaos.com
- **社区**：https://www.ossbar.com

---

<div align="center">

**如果这个项目对您有帮助，欢迎 Star！**

</div>
