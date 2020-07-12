@echo off

:: Build project
echo ************************************************************
echo Building Project...
echo ************************************************************
call mvn -Pwar,release clean package

:: Start application
echo ************************************************************
echo Starting application...
echo ************************************************************
call mvn -Pwlp liberty:create liberty:install-feature liberty:deploy liberty:start

:: Generate API Settings
echo ************************************************************
echo Generating settings for API...
echo ************************************************************
call "C:\Micro Focus\Fortify WebInspect\WISwag.exe" -i ".\etc\WiSwagConfig.json" -ice -it Swagger -wiOutput ApiSettings.xml

:: Execute dynamic scan
echo ************************************************************
echo Executing dynamic scan...
echo ************************************************************
call "C:\Micro Focus\Fortify WebInspect\WI.exe" -s ".\etc\DefaultSettings.xml" -macro ".\etc\Login.webmacro" -u "https://localhost:6443/iwa/" -ep ".\target\wi-iwa.fpr" -ps 1008

:: Stop the application
echo ************************************************************
echo Stopping the application...
echo ************************************************************
call mvn -Pwlp liberty:stop

echo ************************************************************
echo Generating report
echo ************************************************************
call ReportGenerator -user "Demo User" -format pdf -f .\target\wi-iwa.pdf -source .\target\iwa.fpr

