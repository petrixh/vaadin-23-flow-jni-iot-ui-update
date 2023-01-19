#include <jni.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <errno.h>
#include <string.h>
#include <sys/types.h>
#include <time.h>
#include "com_example_application_service_IoTCSocketServer.h"

//Original sources, converted to C++ and modified:
//https://www.thegeekstuff.com/2011/12/c-socket-programming/

// Calling Java from JNI:
//https://stackoverflow.com/questions/28042285/calling-a-java-method-from-the-native-code-using-jni

JNIEXPORT void JNICALL Java_com_example_application_service_IoTCSocketServer_startServer(JNIEnv *env, jobject thisObj, jint port){

    // Get the class from the object we got passed in
    jclass cls_foo = env->GetObjectClass(thisObj);

    // get the method IDs from that class
    jmethodID mid_callback = env->GetMethodID(cls_foo, "callback"       , "()V");
    jmethodID mid_callback2 = env->GetMethodID(cls_foo, "callback2"       , "(Ljava/lang/String;)Ljava/lang/String;");

    int listenfd = 0, connfd = 0;
    struct sockaddr_in serv_addr; 

    char sendBuff[4];
    char recvBuff[4];
    time_t ticks; 

    listenfd = socket(AF_INET, SOCK_STREAM, 0);
    memset(&serv_addr, '0', sizeof(serv_addr));
    memset(sendBuff, '0', sizeof(sendBuff)); 

    serv_addr.sin_family = AF_INET;
    serv_addr.sin_addr.s_addr = htonl(INADDR_ANY);
    serv_addr.sin_port = htons(5000); 

    bind(listenfd, (struct sockaddr*)&serv_addr, sizeof(serv_addr)); 

    listen(listenfd, 10);

    while(1)
    {
        env->CallVoidMethod(thisObj, mid_callback);
        connfd = accept(listenfd, (struct sockaddr*)NULL, NULL);

        printf("\n New client \n");
        //Read from socket
        int n = 0;
        while ( (n = read(connfd, recvBuff, sizeof(recvBuff)-1)) > 0)
        {
            recvBuff[n] = 0;
            if(fputs(recvBuff, stdout) == EOF)
            {
                printf("\n Error : Fputs error\n");
            }

            // A more experienced C++ dev could probably figure out why the
            // EOF doesn't get triggered... Anyways, we'll only read once..
            if(n > 0){
                break;
            }
        }

        printf("\n Done reading... \n");
        //Convert read value to Java String
        jstring jStringParam = env->NewStringUTF( recvBuff );

        //Call Java method with message and return value...
        jstring rv = (jstring)env->CallObjectMethod(thisObj, mid_callback2, jStringParam);

        //Convert return value to char pointer...
        const char *strReturn = env->GetStringUTFChars(rv, 0);

        //Send to client...
        write(connfd, strReturn, strlen(strReturn));

        //Close connection and sleep for 1 second...
        close(connfd);
        sleep(1);
     }
}
