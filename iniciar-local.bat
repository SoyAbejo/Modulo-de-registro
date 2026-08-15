@echo off
setlocal
chcp 65001 >nul
cd /d "%~dp0"

set "PUERTO=%~1"
if "%PUERTO%"=="" set "PUERTO=8081"

echo.
echo Iniciando PetServices en http://localhost:%PUERTO%/petservices
echo Presiona Ctrl+C para detener el servidor.
echo.

call mvn tomcat7:run -Dapp.port=%PUERTO%
