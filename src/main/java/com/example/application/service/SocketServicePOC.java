package com.example.application.service;

import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Service
public class SocketServicePOC {

    private IoTCSocketServer jniSocketServer;

    private List<String> jniMessages = new ArrayList<>();
    private List<JniSocketServiceObserver> observers = new ArrayList<>();

    //Observer for the JniSocketServer singleton..
    private IoTCSocketServer.Observer observer = new IoTCSocketServer.Observer() {
        @Override
        public String onMessage(String message) {
            //Store the message somewhere.. in this case a list..
            jniMessages.add(message);

            //Notify any observers, in this case views...
            for (JniSocketServiceObserver observer : observers) {
                observer.onMessage(message);
            }

            //A nasty way to do addition and implicit string conversion... but it works.. POC...
            return 1000 + Integer.parseInt(message.trim()) + "";
        }
    };

    //After creation, start the server(s) and set observers
    @PostConstruct
    protected void postConstruct() {
        jniSocketServer = new IoTCSocketServer();
        jniSocketServer.setObserver(observer);
    }

    /**
     * Simple observers for the JNI implementation, only supports one UI (browser tab) at a time
     * as the return value controls the response to the client... See other socket example with
     * void interface methods that can support multiple listeners... How this is handled depends
     * on the desired business logic...
     */
    public void addJniSocketServiceObserver(JniSocketServiceObserver observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    /**
     * Removes the JniObserver...
     */
    public void removeJniObserver(JniSocketServiceObserver observer) {
        if (observers.contains(observer)) {
            observers.remove(observer);
        }
    }

    /**
     * Public interface for {@link JniSocketServiceObserver} s
     */
    public interface JniSocketServiceObserver {
        void onMessage(String message);
    }

}
