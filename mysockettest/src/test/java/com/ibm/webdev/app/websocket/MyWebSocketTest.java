package com.ibm.webdev.app.websocket;

import java.net.URI;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
//import java.util.concurrent.atomic.AtomicInteger;

import com.ibm.webdev.app.websocket.client.MyWebSocketClient;

public class MyWebSocketTest {

    //private static final AtomicInteger count = new AtomicInteger(0);

    public static void main(String[] args) {
        URI uri = URI.create("ws://localhost:8081/websocket?type=ws-publisher");
        MyWebSocketClient client = new MyWebSocketClient(uri);
        try {
            // 在连接成功之前会一直阻塞
            client.connectBlocking();

            Timer timer = new Timer();
            MyTimerTask timerTask = new MyTimerTask(client);
            timer.schedule(timerTask, 1000, 2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    static class MyTimerTask extends TimerTask {

        private final MyWebSocketClient client;

        public MyTimerTask(MyWebSocketClient client) {
            this.client = client;
        }

        @Override
        public void run() {
        	String[] name = {"满江红","流浪地球2", "无名","流浪地球","狂飙"};
            client.send(name[new Random().nextInt(5)]+":"+String.valueOf(new Random().nextInt(9000)));
        }
    }
}
