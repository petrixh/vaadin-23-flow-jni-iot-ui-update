#!/bin/bash

echo "Make sure this points to yor JAVA_HOME"
echo $JAVA_HOME
#export JAVA_HOME=/usr/lib/jvm/adoptopenjdk-11-hotspot-amd64

#Wipe native libs...
rm -r ./native-lib
mkdir native-lib

#Socket JNI server client example
javac -h ./target/native ./src/main/java/com/example/application/service/IoTCSocketServer.java -d target/native
cp native/com_example_application_service_IoTCSocketServer.cpp target/native
g++ -c -fPIC -I${JAVA_HOME}/include -I${JAVA_HOME}/include/linux target/native/com_example_application_service_IoTCSocketServer.cpp -o target/native/com_example_application_service_IoTCSocketServer.o
g++ -shared -fPIC -o target/native/libsockserv.so target/native/com_example_application_service_IoTCSocketServer.o -lc

## TODO make any other native libs...

cp target/native/*.so native-lib/

######
# After making the libX.so file, make sure you add it to the Java runtime with:
# -Djava.library.path=/abs/path/to/libs...
# Or you can try a relative path but your mileage may vary
# -Djava.library.path=./native-lib
#