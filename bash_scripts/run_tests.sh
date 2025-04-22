#!/bin/bash
cd ..
java -jar lib/junit-platform-console-standalone-1.13.0-M2.jar \
--class-path out \
--scan-class-path