@echo off
chcp 65001 >nul 2>&1
title BudaOS 安装向导

echo ========================================
echo    BudaOS 学习平台 - 安装向导
echo ========================================
echo.

:: 检查 Java 环境
echo [1/4] 检测 Java 环境...
java -version >nul 2>&1
if errorlevel 1 (
    echo [错误] 未检测到 Java 环境，请先安装 JDK 1.8+
    echo 下载地址: https://adoptium.net/
    pause
    exit /b 1
)
for /f "tokens=*" %%i in ('java -version 2^>^&1') do (
    echo        %%i
)

:: 检查 Maven 环境
echo.
echo [2/4] 检测 Maven 环境...
mvn -version >nul 2>&1
if errorlevel 1 (
    echo [错误] 未检测到 Maven 环境，请先安装 Maven 3.0+
    echo 下载地址: https://maven.apache.org/download.cgi
    pause
    exit /b 1
)
for /f "tokens=*" %%i in ('mvn -version 2^>^&1 ^| findstr /i "apache"') do (
    echo        %%i
)

:: 检查端口占用
echo.
echo [3/4] 检测端口 9080...
netstat -ano | findstr ":9080" >nul 2>&1
if not errorlevel 1 (
    echo [警告] 端口 9080 已被占用，可能导致启动失败
    echo         请关闭占用端口的程序或修改配置
)

:: 检查 MySQL 客户端
echo.
echo [4/4] 检测 MySQL 客户端...
mysql --version >nul 2>&1
if errorlevel 1 (
    echo [警告] 未检测到 MySQL 客户端命令行工具
    echo         建议安装 MySQL Server 或 MySQL Workbench
    echo         下载地址: https://dev.mysql.com/downloads/mysql/
)

echo.
echo ========================================
echo    环境检测完成
echo ========================================
echo.
echo 正在构建项目，请稍候...
echo.

:: Maven 打包
cd /d "%~dp0budaos-monolith"
call mvn clean package -DskipTests -q
if errorlevel 1 (
    echo.
    echo [错误] 项目构建失败！
    echo         请检查代码或依赖配置
    pause
    exit /b 1
)

:: 启动应用（首次启动进入安装向导）
echo.
echo ========================================
echo    构建成功，正在启动应用...
echo ========================================
echo.
echo    请访问以下地址完成安装向导:
echo.
echo    http://localhost:9080/install.html
echo.
echo    应用启动后，此窗口将显示运行日志
echo.

:: 启动应用
cmd /c "set JAVA_TOOL_OPTIONS=-Dfile.encoding=UTF-8 && java -Dfile.encoding=UTF-8 -jar budaos-application\target\budaos-monolith.jar --spring.profiles.active=dev"

pause
