package com.example.application.service;

import java.util.ArrayList;

/**
 * A simple POC demonstrating JNI based socket communication where Java logic controls the response.
 */
public class IoTCSocketServer {

    static {
        System.loadLibrary("sockserv");
    }

    private Observer observer = null;

    public native void startServer(int port);

    //https://stackoverflow.com/questions/28042285/calling-a-java-method-from-the-native-code-using-jni
    public void callback() {
        System.out.println("JNI socket server waiting for a connection...");
    }

    public String callback2(String value) {
        //The \r\n terminators are coming through.. Hence trim..
        value = value.trim();
        System.out.println("JNI Callback with value: " + value);

        String retVal = "-1";
        if (observer == null) {
            Integer clientVal = Integer.parseInt(value);
            // Server responds with client + 9000...
            retVal = 9000 + clientVal + "";
        } else {
            //Let the observer take control...
            retVal = observer.onMessage(value);
        }

        return retVal;
    }

    public IoTCSocketServer() {

        //Not the best way to handle, but POC...
        Thread thread = new Thread(() -> {

            // Port is not actually mapped through...
            // should be simple jint mapping though...
            startServer(5000);
        });

        thread.start();
    }

    // Add a simple observer as a listener for the events...
    public void setObserver(Observer observer) {
        this.observer = observer;
    }

    // Remove observer when no longer needed...
    public void removeObserver() {
        observer = null;
    }

    public interface Observer {
        String onMessage(String message);
    }


}
