@echo off
chcp 65001 >nul
REM ═══════════════════════════════════════════════════════════════════════
REM  desplegar.bat — Reconstrucción y verificación de PetServices
REM ═══════════════════════════════════════════════════════════════════════
REM  Resuelve el error 404 de los módulos (citas, servicios, etc.):
REM  reconstruye el WAR desde cero, verifica que contiene los módulos
REM  nuevos y avisa si hay un Tomcat activo que esté sirviendo una
REM  versión vieja (causa del 404).
REM ═══════════════════════════════════════════════════════════════════════
cd /d "%~dp0"

echo.
echo [1/4] Comprobando si hay un servidor activo en el puerto 8080...
netstat -ano | findstr ":8080" | findstr "LISTENING" >nul 2>&1
if not errorlevel 1 (
    echo   [AVISO] Hay un proceso escuchando en el puerto 8080.
    echo   DETENLO ANTES de continuar, o el build puede fallar por
    echo   archivos bloqueados y el servidor seguira sirviendo la
    echo   version vieja (con el 404).
    echo     - Si usas "mvn tomcat7:run": presiona Ctrl+C en esa terminal.
    echo     - Si usas Eclipse/IntelliJ: boton Stop del servidor.
    echo.
    pause
) else (
    echo   [OK] No hay proceso escuchando en el puerto 8080.
)

echo.
echo [2/4] Limpiando builds anteriores (incluye la cache de Tomcat)...
call mvn clean -q
if errorlevel 1 (
    echo   [ERROR] No se pudo limpiar. Cierra el servidor e intenta de nuevo.
    pause
    exit /b 1
)

echo.
echo [3/4] Compilando y empaquetando el WAR...
call mvn package -q -DskipTests
if errorlevel 1 (
    echo   [ERROR] El build fallo. Revisa los errores de compilacion.
    pause
    exit /b 1
)

echo.
echo [4/4] Verificando que el WAR contiene los modulos nuevos...
set "OK=1"
if not exist "target\petservices\WEB-INF\classes\com\petservices\servlet\ServicioServlet.class"  (set "OK=0" & echo   [FALTA] ServicioServlet.class)
if not exist "target\petservices\WEB-INF\classes\com\petservices\servlet\DashboardServlet.class" (set "OK=0" & echo   [FALTA] DashboardServlet.class)
if not exist "target\petservices\vistas\servicios.jsp"  (set "OK=0" & echo   [FALTA] vistas/servicios.jsp)
if not exist "target\petservices\vistas\fragmentos\sidebar.jsp" (set "OK=0" & echo   [FALTA] vistas/fragmentos/sidebar.jsp)

if "%OK%"=="1" (
    echo   [OK] El WAR esta completo y listo: target\petservices.war
) else (
    echo   [ERROR] Faltan archivos en el build. Revisa el proyecto.
    pause
    exit /b 1
)

echo.
echo ═══════════════════════════════════════════════════════════════
echo  LISTO. Ahora inicia el servidor:
echo.
echo   Opcion A - Tomcat real (recomendado):
echo     1. Deten Tomcat
echo     2. Borra en TOMCAT_HOME\webapps la carpeta petservices y el
echo        archivo petservices.war viejos
echo     3. Borra TOMCAT_HOME\work\Catalina\localhost\petservices
echo     4. Copia target\petservices.war a TOMCAT_HOME\webapps\
echo     5. Inicia Tomcat y abre http://localhost:8080/petservices/
echo.
echo   Opcion B - Plugin Maven embebido:
echo     mvn tomcat7:run
echo     (abre http://localhost:8080/petservices/)
echo ═══════════════════════════════════════════════════════════════
pause
