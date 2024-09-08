@echo off
REM Set the path to the JAR file
set JAR_PATH="E:\questions-interview-automation\build\libs\questions-interview-automation-0.0.1-SNAPSHOT.jar"

REM Start the Spring Boot application
echo Starting Spring Boot application...
java -jar %JAR_PATH%

pause