@echo off
echo "Copying insecure examples - this will overwrite your codebase."
:choice
set /P c=Are you sure you want to continue[Y/N]?
if /I "%c%" EQU "Y" goto :do_copy
if /I "%c%" EQU "N" goto :dont_copy
goto :choice

:do_copy
echo "Starting copy"
xcopy /f /i /r /s /u /y .\etc\insecure-examples\src .\src
pause
exit

:dont_copy
echo "Stopping"
pause
exit
