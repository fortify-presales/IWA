@echo off

:: Ensure JAVA_HOME is set to a relevant JDK 1.8 version

:: Clean Project and scan results from previous run
echo ************************************************************
echo Cleaning project and scan results from previous run...
echo ************************************************************
call mvn clean
call sourceanalyzer -Dcom.fortify.sca.ProjectRoot=.fortify -b iwa -clean

:: Compile the application
echo ************************************************************
echo Re-compile application in debug mode...
echo ************************************************************
call mvn verify -P release,jar -DskipTests -Dmaven.compiler.debuglevel="lines,vars,source"
call mvn dependency:build-classpath -Dmdep.outputFile=target\cp.txt
set /p CP=<target\cp.txt

:: Translate source files
echo ************************************************************
echo Translating source files...
echo ************************************************************
call sourceanalyzer -Dcom.fortify.sca.ProjectRoot=.fortify -b iwa -jdk 1.8  -cp %CP% "src/main/java/**/*" "src/main/resources/**/*"

:: Scan the application
echo ************************************************************
echo Scanning the application...
echo ************************************************************
call sourceanalyzer -Dcom.fortify.sca.ProjectRoot=.fortify -b iwa -findbugs -cp %CP%  ‑build‑project "Insecure Web App" -build-version "v1.0" -build-label "SNAPSHOT" -scan -filter etc\sca-filter.txt -f target\iwa.fpr

echo ************************************************************
echo Generating report
echo ************************************************************
call ReportGenerator -Dcom.fortify.sca.ProjectRoot=.fortify -user "Demo User" -format pdf -f target\iwa.pdf -source target\iwa.fpr
