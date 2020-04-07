@echo off

:: Clean Project and scan results from previous run
echo ************************************************************
echo Cleaning project and scan results from previous run...
echo ************************************************************
call mvn clean
call sourceanalyzer -Dcom.fortify.sca.ProjectRoot=.fortify -b simple-secure-app -clean

:: Compile the application
echo ************************************************************
echo Re-compile application in debug mode...
echo ************************************************************
call mvn verify -P release,jar -DskipTests -Dmaven.compiler.debuglevel="lines,vars,source"

:: Translate source files
echo ************************************************************
echo Translating source files...
echo ************************************************************
call sourceanalyzer -Dcom.fortify.sca.ProjectRoot=.fortify -Dcom.fortify.sca.Xmx=3G -b simple-secure-app "src/main/java/**/*.java" "src/main/resource/**/*.*"

:: Scan the application
echo ************************************************************
echo Scanning the application...
echo ************************************************************
call sourceanalyzer -Dcom.fortify.sca.ProjectRoot=.fortify -b simple-secure-app -scan -f secure-web-app.fpr

echo ************************************************************
echo Generating report
echo ************************************************************
call ReportGenerator -Dcom.fortify.sca.ProjectRoot=.fortify -user "Demo User" -format pdf -f secure-web-app.pdf -source secure-web-app.fpr
pause
