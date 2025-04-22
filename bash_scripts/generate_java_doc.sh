#!/bin/bash
cd ..
javadoc -d html -author -private -noqualifier all -version \
-sourcepath src \
boundary control model model.enums repo util src/MainApp.java