@echo off
setlocal

:: Check if keytool exists
where keytool >nul 2>&1
if %errorlevel% neq 0 (
    echo Error: keytool is not available. Please ensure that it is installed and in your PATH.
    goto end
)

:: Define the keystore folder and create it if not existing
set SCRIPT_DIR=%~dp0
set KEYSTORE_DIR=%SCRIPT_DIR%keystore

if not exist "%KEYSTORE_DIR%" (
    mkdir "%KEYSTORE_DIR%"
)

set KEYSTORE_NAME=springboot.p12
set ALIAS=api
set VALIDITY=3650
set KEY_SIZE=2048
set KEYPASS=123456

echo Creating the keystore...
keytool -genkeypair -alias %ALIAS% -keyalg RSA -keysize %KEY_SIZE% -storetype PKCS12 -keystore "%KEYSTORE_DIR%\%KEYSTORE_NAME%" -validity %VALIDITY% -storepass %KEYPASS% -keypass %KEYPASS% -dname "CN=Unknown, OU=Unknown, O=Unknown, L=Unknown, S=Unknown, C=Unknown" >nul 2>&1
if %errorlevel% neq 0 (
    echo Error: Failed to create the keystore. Please check your inputs and try again.
    goto end
)

echo Keystore %KEYSTORE_NAME% created successfully in %KEYSTORE_DIR%.
goto end

:end
endlocal
