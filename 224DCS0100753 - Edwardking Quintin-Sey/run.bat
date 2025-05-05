@echo off
echo GPA Calculator Application
echo ========================

REM Set the classpath to include the application classes and MySQL connector JAR
set CLASSPATH=.;./lib/mysql-connector-j-92.0.jar;./src

REM Compile the Java files
echo Compiling Java files...
javac src/models/*.java src/db/*.java src/ui/*.java

REM Run the application
echo Starting the application...
java -cp %CLASSPATH% ui.GPACalculatorUI

pause

