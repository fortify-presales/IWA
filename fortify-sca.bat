@echo off

:: Clean Project and scan results from previous run
echo "Cleaning project and scan results from previous run.."
call mvn clean
call sourceanalyzer -b simple-secure-app -clean

:: Compile the application
echo "Re-compile application in debug mode.."
call mvn verify -DskipTests -Dmaven.compiler.debuglevel=lines,vars,source

:: Translate source files
echo "Translating source files..."
call sourceanalyzer -b simple-secure-app "src/**/*.java", "src/**/*.html"

:: Scan the application
echo "Scanning the application..."
call sourceanalyzer -b simple-secure-app -scan -f simple-secure-app.fpr

echo "Scan complete..."
pause
