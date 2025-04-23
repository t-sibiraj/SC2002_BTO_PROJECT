#!/bin/bash
cd ..
javac -d out -cp "lib/junit-platform-console-standalone-1.13.0-M2.jar:out:src" $(find test -name "*.java") 
java -jar lib/junit-platform-console-standalone-1.13.0-M2.jar \
execute \
--class-path out \
--scan-class-path