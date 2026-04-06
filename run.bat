@echo off
if not exist bin mkdir bin
echo Compiling...
javac -d bin -cp "lib/mysql-connector-j-9.6.0.jar;lib/jbcrypt-0.4.jar;lib/javax.mail.jar;lib/activation.jar;src" src/studentcourseregistrationsystem/*.java src/user/*.java
if %errorlevel% neq 0 (
    echo Compilation failed!
    pause
    exit /b %errorlevel%
)
echo Copying resources...
xcopy /E /I /Y src\icons bin\icons >nul
echo Running application...
java -cp "bin;lib/mysql-connector-j-9.6.0.jar;lib/jbcrypt-0.4.jar;lib/javax.mail.jar;lib/activation.jar" studentcourseregistrationsystem.Main
pause
