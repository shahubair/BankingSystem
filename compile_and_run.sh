#!/bin/bash
# Shell script to compile and run the Multithreaded Banking System Project on Unix/Linux/macOS

echo "========================================================================="
echo "  Compiling Java Source Files..."
echo "========================================================================="

# Create the output directory if it does not exist
mkdir -p bin

# Compile all java source files under src/ into bin/
javac -d bin src/com/banking/exception/*.java src/com/banking/model/*.java src/com/banking/task/*.java src/com/banking/monitor/*.java src/com/banking/main/*.java

if [ $? -ne 0 ]; then
    echo "[ERROR] Compilation failed!"
    exit 1
fi

echo "[SUCCESS] Compilation complete. Compiled files stored in 'bin/'"
echo ""
echo "========================================================================="
echo "  Running BankingSystemApp..."
echo "========================================================================="

# Run the main class specifying classpath (bin/)
java -cp bin com.banking.main.BankingSystemApp

echo ""
echo "========================================================================="
echo "  Execution Finished."
echo "========================================================================="
