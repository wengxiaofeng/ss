package com.wxf.test.scrcpy;

import com.wxf.test.MyWebSocket;
import lombok.Data;
import java.util.Queue;

@Data
public class StreamSender implements Runnable {

    private Queue<byte[]> dataQueue;
    private MyWebSocket socket;

    public StreamSender(Queue<byte[]> dataQueue, MyWebSocket socket) {
        this.dataQueue = dataQueue;
        this.socket = socket;
    }


    public void run() {
        while (socket.getSession().isOpen()) {
            if (dataQueue.isEmpty()) {
                continue;
            }
            byte[] buffer = dataQueue.poll();
            try {
                socket.sendMessage(buffer);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

}
