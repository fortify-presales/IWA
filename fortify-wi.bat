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

:: Execute dynamic scan
echo ************************************************************
echo Executing dynamic scan...
echo ************************************************************
call "C:\Micro Focus\Fortify WebInspect\WI.exe" -s ".\etc\DefaultSettings.xml" -macro ".\etc\Login.webmacro" -u "https://localhost:6443/secure-web-app/" -ep ".\target\wi-secure-web-app.fpr" -ps 1008

:: Stop the application
echo ************************************************************
echo Stopping the application...
echo ************************************************************
call mvn -Pwlp liberty:stop

echo ************************************************************
echo Generating report
echo ************************************************************
call ReportGenerator -user "Demo User" -format pdf -f .\target\wi-secure-web-app.pdf -source .\target\wi-secure-web-app.fpr

