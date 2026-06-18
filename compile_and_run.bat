@echo off
REM Windows batch script to compile and run the Multithreaded Banking System Project

echo =========================================================================
echo   Compiling Java Source Files...
echo =========================================================================

REM Create the output directory if it does not exist
if not exist bin (
    mkdir bin
)

REM Compile all java source files under src/ into bin/
javac -d bin src\com\banking\exception\*.java src\com\banking\model\*.java src\com\banking\task\*.java src\com\banking\monitor\*.java src\com\banking\main\*.java

if %errorlevel% neq 0 (
    echo [ERROR] Compilation failed!
    exit /b %errorlevel%
)

echo [SUCCESS] Compilation complete. Compiled files stored in 'bin/'
echo.
echo =========================================================================
echo   Running BankingSystemApp...
echo =========================================================================

REM Run the main class specifying classpath (bin/)
java -cp bin com.banking.main.BankingSystemApp

echo.
echo =========================================================================
echo   Execution Finished.
echo =========================================================================
