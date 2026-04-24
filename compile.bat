@echo off
REM Script para compilar o mod no Windows
REM Ajuste o caminho do JAVA_HOME abaixo para o local do seu JDK no Windows

set JAVA_HOME=C:\Users\lucas\.jdks\liberica-full-26
set MAVEN_BIN=C:\Program Files\JetBrains\IntelliJ IDEA 2026.1\plugins\maven\lib\maven3\bin
set PATH=%JAVA_HOME%\bin;%MAVEN_BIN%;%PATH%

echo Usando Java de: %JAVA_HOME%
java -version

echo Iniciando compilacao com Maven...
mvn clean package

pause
