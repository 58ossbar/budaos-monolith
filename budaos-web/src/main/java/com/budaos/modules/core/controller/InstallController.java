package com.budaos.modules.core.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.budaos.core.baseclass.domain.R;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.RuntimeUtil;

/**
 * 安装向导控制器
 */
@RestController
@RequestMapping("/api/install")
public class InstallController {

    private static final Logger log = LoggerFactory.getLogger(InstallController.class);

    /**
     * 获取安装状态
     */
    @GetMapping("/status")
    public R getStatus() {
        Map<String, Object> status = new HashMap<>();

        // 检查是否已安装
        String configPath = getConfigFilePath();
        File configFile = new File(configPath);
        status.put("installed", configFile.exists() && isConfigValid());

        // 检查环境（只检查 Java，不再依赖 MySQL 和 Maven 命令行工具）
        status.put("javaAvailable", true); // 能运行此代码说明 Java 已安装
        status.put("mavenAvailable", false); // 不再需要 Maven
        status.put("mysqlAvailable", false); // 不再需要 MySQL 命令行，使用 JDBC 连接

        R r = R.ok();
        r.put("data", status);
        return r;
    }

    /**
     * 检测 MySQL 连接
     */
    @PostMapping("/check/mysql")
    public R checkMySQL(@RequestBody Map<String, String> params) {
        String host = params.getOrDefault("host", "localhost");
        String port = params.getOrDefault("port", "3306");
        String username = params.getOrDefault("username", "root");
        String password = params.getOrDefault("password", "");

        try {
            String url = String.format("jdbc:mysql://%s:%s?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai",
                    host, port);
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection conn = DriverManager.getConnection(url, username, password)) {
                R r = R.ok();
                r.put("msg", "MySQL 连接成功");
                return r;
            }
        } catch (Exception e) {
            log.error("MySQL 连接失败", e);
            return R.error("MySQL 连接失败: " + e.getMessage());
        }
    }

    /**
     * 检测 Redis 连接
     */
    @PostMapping("/check/redis")
    public R checkRedis(@RequestBody Map<String, String> params) {
        String host = params.getOrDefault("host", "localhost");
        String port = params.getOrDefault("port", "6379");
        String password = params.getOrDefault("password", "");

        try {
            String cmd = String.format("redis-cli -h %s -p %s %s ping",
                host, port, password.isEmpty() ? "" : ("-a " + password));
            Process process = RuntimeUtil.exec(cmdArray(cmd));
            String result = readStream(process.getInputStream());
            int exitCode = process.waitFor();

            if (exitCode == 0 && result != null && result.trim().equalsIgnoreCase("PONG")) {
                R r = R.ok();
                r.put("msg", "Redis 连接成功");
                return r;
            }
            return R.error("Redis 连接失败: " + result);
        } catch (Exception e) {
            return R.error("Redis 连接失败: " + e.getMessage());
        }
    }

    /**
     * 初始化数据库
     */
    @PostMapping("/init/database")
    public R initDatabase(@RequestBody Map<String, String> params) {
        String host = params.getOrDefault("host", "localhost");
        String port = params.getOrDefault("port", "3306");
        String username = params.getOrDefault("username", "root");
        String password = params.getOrDefault("password", "");
        String database = params.getOrDefault("database", "budaos");

        try {
            // 创建数据库（使用 JDBC）
            String url = String.format("jdbc:mysql://%s:%s?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true",
                    host, port);
            
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // 先创建数据库（如果不存在）
            try (Connection conn = DriverManager.getConnection(url, username, password);
                 Statement stmt = conn.createStatement()) {
                
                log.info("创建数据库: {}", database);
                stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + database + 
                    " DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci");
                log.info("数据库创建成功");
            }
            
            // 导入 SQL 脚本
            String sqlFilePath = getSqlFilePath();
            File sqlFile = new File(sqlFilePath);
            if (!sqlFile.exists()) {
                return R.error("SQL 脚本文件不存在: " + sqlFilePath);
            }
            
            // 读取 SQL 文件并执行
            log.info("开始导入 SQL 脚本: {}", sqlFilePath);
            String sqlContent = FileUtil.readString(sqlFile, StandardCharsets.UTF_8);
            
            // 连接到目标数据库并执行 SQL
            String dbUrl = String.format("jdbc:mysql://%s:%s/%s?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true",
                    host, port, database);
            
            try (Connection conn = DriverManager.getConnection(dbUrl, username, password);
                 Statement stmt = conn.createStatement()) {
                
                // 分割 SQL 语句（按分号分割）
                String[] sqlStatements = sqlContent.split(";");
                int executedCount = 0;
                
                for (String sql : sqlStatements) {
                    String trimmedSql = sql.trim();
                    // 跳过空语句和注释
                    if (trimmedSql.isEmpty() || trimmedSql.startsWith("--") || trimmedSql.startsWith("/*")) {
                        continue;
                    }
                    
                    try {
                        stmt.execute(trimmedSql);
                        executedCount++;
                    } catch (Exception e) {
                        // 忽略已存在的表等错误
                        log.debug("执行 SQL 语句时出现异常（可能已存在）: {}", e.getMessage());
                    }
                }
                
                log.info("数据库初始化成功，共执行 {} 条 SQL 语句", executedCount);
                R r = R.ok();
                r.put("msg", "数据库初始化成功，共执行 " + executedCount + " 条 SQL 语句");
                return r;
            }
            
        } catch (Exception e) {
            log.error("数据库初始化异常", e);
            return R.error("数据库初始化异常: " + e.getMessage());
        }
    }

    /**
     * 保存配置
     */
    @PostMapping("/save/config")
    public R saveConfig(@RequestBody Map<String, String> params) {
        try {
            StringBuilder config = new StringBuilder();
            config.append("# BudaOS 配置文件\n");
            config.append("# 由安装向导生成\n\n");

            // 数据库配置
            config.append("spring:\n");
            config.append("  datasource:\n");
            config.append("    url: jdbc:mysql://").append(params.get("dbHost")).append(":")
                  .append(params.get("dbPort")).append("/")
                  .append(params.get("dbName")).append("?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai\n");
            config.append("    username: ").append(params.get("dbUsername")).append("\n");
            config.append("    password: ").append(params.get("dbPassword")).append("\n");
            config.append("\n");

            // Redis 配置
            config.append("  redis:\n");
            config.append("    host: ").append(params.get("redisHost")).append("\n");
            config.append("    port: ").append(params.get("redisPort")).append("\n");
            config.append("    password: ").append(params.get("redisPassword")).append("\n");
            config.append("\n");

            // 应用配置
            config.append("com.budaos:\n");
            config.append("  file-upload-path: ").append(params.get("uploadPath")).append("\n");
            config.append("  baseUrl: http://localhost:9080\n");

            // 确保目录存在
            File configDir = new File(getConfigOverridePath()).getParentFile();
            if (configDir != null && !configDir.exists()) {
                configDir.mkdirs();
            }

            // 写入配置文件
            String configPath = getConfigOverridePath();
            FileUtil.writeString(config.toString(), configPath, StandardCharsets.UTF_8);

            log.info("配置文件保存成功: {}", configPath);
            R r = R.ok();
            r.put("msg", "配置保存成功");
            return r;
        } catch (Exception e) {
            log.error("配置保存失败", e);
            return R.error("配置保存失败: " + e.getMessage());
        }
    }

    /**
     * 完成安装
     */
    @PostMapping("/complete")
    public R complete() {
        try {
            // 创建安装完成标记文件
            File markerFile = new File(getInstallMarkerPath());
            FileUtil.writeString("installed", markerFile, StandardCharsets.UTF_8);

            log.info("安装完成");
            R r = R.ok();
            r.put("msg", "安装完成");
            return r;
        } catch (Exception e) {
            return R.error("安装完成标记失败: " + e.getMessage());
        }
    }

    // ==================== 私有方法 ====================

    private boolean isCommandAvailable(String command) {
        try {
            Process process = RuntimeUtil.exec(command);
            return process.waitFor() == 0;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isConfigValid() {
        String configPath = getConfigOverridePath();
        File file = new File(configPath);
        return file.exists() && file.length() > 0;
    }

    private String getConfigFilePath() {
        return System.getProperty("user.dir") + "/config/override.yml";
    }

    private String getConfigOverridePath() {
        return System.getProperty("user.dir") + "/config/override.yml";
    }

    private String getSqlFilePath() {
        return System.getProperty("user.dir") + "/database/db.sql";
    }

    private String getInstallMarkerPath() {
        return System.getProperty("user.dir") + "/.installed";
    }

    private String[] cmdArray(String cmd) {
        if (System.getProperty("os.name").toLowerCase().contains("windows")) {
            return new String[]{"cmd", "/c", cmd};
        } else {
            return new String[]{"/bin/sh", "-c", cmd};
        }
    }

    private String readStream(InputStream inputStream) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            return sb.toString();
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}
