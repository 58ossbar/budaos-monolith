#!/bin/bash

# =========================================
#   BudaOS 学习平台 - 安装向导脚本
# =========================================

# 设置中文字体支持
export LANG=zh_CN.UTF-8

echo "========================================"
echo "   BudaOS 学习平台 - 安装向导"
echo "========================================"
echo ""

# 检查 Java 环境
echo "[1/4] 检测 Java 环境..."
if ! command -v java &> /dev/null; then
    echo "[错误] 未检测到 Java 环境，请先安装 JDK 1.8+"
    echo "下载地址: https://adoptium.net/"
    exit 1
fi
java -version 2>&1 | head -1

# 检查 Maven 环境
echo ""
echo "[2/4] 检测 Maven 环境..."
if ! command -v mvn &> /dev/null; then
    echo "[错误] 未检测到 Maven 环境，请先安装 Maven 3.0+"
    echo "下载地址: https://maven.apache.org/download.cgi"
    exit 1
fi
mvn -version 2>&1 | head -1

# 检查端口占用
echo ""
echo "[3/4] 检测端口 9080..."
if lsof -Pi :9080 -sTCP:LISTEN -t >/dev/null 2>&1 || netstat -an 2>/dev/null | grep ":9080" | grep LISTEN >/dev/null; then
    echo "[警告] 端口 9080 已被占用，可能导致启动失败"
    echo "       请关闭占用端口的程序或修改配置"
fi

# 检查 MySQL 客户端
echo ""
echo "[4/4] 检测 MySQL 客户端..."
if ! command -v mysql &> /dev/null; then
    echo "[警告] 未检测到 MySQL 客户端命令行工具"
    echo "       建议安装 MySQL Server 或 MySQL Workbench"
    echo "       下载地址: https://dev.mysql.com/downloads/mysql/"
fi

echo ""
echo "========================================"
echo "   环境检测完成"
echo "========================================"
echo ""
echo "正在构建项目，请稍候..."
echo ""

# 获取脚本所在目录
SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
cd "$SCRIPT_DIR/budaos-monolith"

# Maven 打包
mvn clean package -DskipTests -q
if [ $? -ne 0 ]; then
    echo ""
    echo "[错误] 项目构建失败！"
    echo "       请检查代码或依赖配置"
    exit 1
fi

echo ""
echo "========================================"
echo "   构建成功，正在启动应用..."
echo "========================================"
echo ""
echo "   请访问以下地址完成安装向导:"
echo ""
echo "   http://localhost:9080/install.html"
echo ""
echo "   应用启动后，此窗口将显示运行日志"
echo ""

# 启动应用
java -Dfile.encoding=UTF-8 -jar budaos-application/target/budaos-monolith.jar --spring.profiles.active=dev
