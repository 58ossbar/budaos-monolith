@echo off
chcp 65001 >nul 2>&1
title BudaOS Monolith Application

echo ========================================
echo   BudaOS Monolith Starting...
echo ========================================
echo.

:: Find project root
set PROJECT_DIR=%~dp0
cd /d "%PROJECT_DIR%"

:: Check if JAR exists
if not exist "budaos-application\target\budaos-monolith.jar" (
    echo [INFO] JAR file not found, starting Maven build...
    echo.
    call mvn clean package -DskipTests
    if errorlevel 1 (
        echo [ERROR] Build failed!
        pause
        exit /b 1
    )
)

:: Start application with UTF-8 encoding
echo [INFO] Starting application on port 9080...
echo.
cmd /c "set JAVA_TOOL_OPTIONS=-Dfile.encoding=UTF-8 && java -Dfile.encoding=UTF-8 -jar budaos-application\target\budaos-monolith.jar --spring.profiles.active=dev"

pause
