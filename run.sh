#!/bin/bash

# Compile Java files
clear
javac -d out MainApp.java */*.java */*/*.java

# If compilation succeeds, run the app
#chmod +x run.sh
#./run.sh
if [ $? -eq 0 ]; then
    cd out
    java MainApp
else
    echo "❌ Compilation failed. Fix the errors before running."
fi