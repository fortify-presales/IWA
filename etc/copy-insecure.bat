@echo off

:: Copy insecure examples into code base
echo ************************************************************
echo Copying insecure examples into code base...
echo ************************************************************
xcopy /f /i /r /s /u /y .\insecure-examples\src ..\src
echo ************************************************************
echo Use the following command to revert the changes:
echo    git reset --hard HEAD
echo ************************************************************
