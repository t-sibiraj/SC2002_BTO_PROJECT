#!/bin/bash
cd ..

# Compile Java files
clear
javac -d out $(find src -name "*.java")

# If compilation succeeds, run the app
#chmod +x run.sh
#./run.sh
if [ $? -eq 0 ]; then
    cd out
    java -cp out MainApp
else
    echo "Compilation failed. Fix the errors before running."
fi