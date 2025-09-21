@echo off
setlocal EnableExtensions EnableDelayedExpansion
title Construir JAR - Sistema de Turnos (Windows)

echo ================================================
echo   Construccion del JAR - Sistema de Turnos
echo   Requiere: JDK 17+ (java, javac, jar en PATH)
echo ================================================
echo(

REM 1) Verificar Java
echo [1/5] Verificando instalacion de Java...
java  -version >nul 2>&1
if errorlevel 1 echo Error: No se encontro 'java'. Instala JDK 17+ y agrega %%JAVA_HOME%%\bin al PATH. & exit /b 1
javac -version >nul 2>&1
if errorlevel 1 echo Error: No se encontro 'javac'. Instala JDK 17+ y agrega %%JAVA_HOME%%\bin al PATH. & exit /b 1
jar   --version >nul 2>&1
if errorlevel 1 echo Error: No se encontro 'jar'. Asegurate de usar la distribucion JDK (no solo JRE). & exit /b 1

REM 2) Limpiar carpetas previas
echo [2/5] Limpiando salidas previas...
if exist out   rmdir /s /q out
if exist build rmdir /s /q build
mkdir out
if errorlevel 1 echo Error creando carpeta out & exit /b 1

REM 3) Encontrar y compilar fuentes
echo [3/5] Compilando fuentes...
if exist sources.txt del /q sources.txt
for /r src %%f in (*.java) do (
  echo %%f>>sources.txt
)

if not exist sources.txt (
  echo Error: No se encontraron archivos .java dentro de la carpeta src
  exit /b 1
)

javac -d out -encoding UTF-8 @sources.txt
if errorlevel 1 echo Error en la compilacion. Revisa los mensajes anteriores. & exit /b 1

REM 4) Crear manifest y empaquetar JAR
echo [4/5] Empaquetando JAR...
mkdir build  >nul 2>&1
mkdir build\libs >nul 2>&1

> out\MANIFEST.MF  echo Manifest-Version: 1.0
>>out\MANIFEST.MF echo Main-Class: labturnos.MainFrame

jar cfm build\libs\turnos-final.jar out\MANIFEST.MF -C out .
if errorlevel 1 echo Error al crear el JAR. & exit /b 1

REM 5) Confirmar y sugerir ejecucion
echo [5/5] Listo. JAR generado en: build\libs\turnos-final.jar
echo(
echo Para ejecutar:
echo   java -jar build\libs\turnos-final.jar
echo(
endlocal
