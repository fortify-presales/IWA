@echo off

:: Clean Project and scan results from previous run
echo --------------------------------------------------
echo Cleaning project and scan results from previous run...
echo --------------------------------------------------
call mvn clean
call sourceanalyzer -Dcom.fortify.sca.ProjectRoot=.fortify -b simple-secure-app -clean

:: Compile the application
echo --------------------------------------------------
echo Re-compile application in debug mode...
echo --------------------------------------------------
call mvn verify -DskipTests -Dmaven.compiler.debuglevel=lines,vars,source

:: Translate source files
echo --------------------------------------------------
echo Translating source files...
echo --------------------------------------------------
call sourceanalyzer -Dcom.fortify.sca.ProjectRoot=.fortify -Dcom.fortify.sca.Xmx=3G -b simple-secure-app "src/**/*.java", "src/**/*.html"

:: Scan the application
echo --------------------------------------------------
echo Scanning the application...
echo --------------------------------------------------
call sourceanalyzer -Dcom.fortify.sca.ProjectRoot=.fortify -b simple-secure-app -scan -f secure-web-app.fpr

echo --------------------------------------------------
echo Scan complete...
echo --------------------------------------------------

pause
