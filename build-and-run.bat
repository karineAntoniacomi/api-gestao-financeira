@echo off
REM Script para buildar microsserviços e iniciar Docker Compose (Windows)

setlocal

REM Definir array de pastas dos microsserviços
set SERVICES=ms-service-usuario ms-service-transacao ms-processor-consumer

REM Guardar diretório raiz
set ROOT_DIR=%CD%

REM Loop pelos microsserviços
for %%S in (%SERVICES%) do (
    echo ================================
    echo Buildando %%S...
    echo ================================
    if exist "%ROOT_DIR%\%%S" (
        cd "%ROOT_DIR%\%%S"
        mvn clean package
        if errorlevel 1 (
            echo Erro ao buildar %%S
            pause
            exit /b 1
        )
        cd "%ROOT_DIR%"
    ) else (
        echo Pasta %%S nao existe, pulando...
    )
)

echo ================================
echo Todos os microsserviços buildados. Iniciando Docker Compose...
echo ================================

REM Ajuste dependendo da versão do Docker
docker-compose up --build

pause
endlocal
