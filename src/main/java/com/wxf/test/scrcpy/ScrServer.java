/**
 *
 */
package com.wxf.test.scrcpy;

import com.wxf.test.MyWebSocket;
import lombok.Data;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

@Data
public class ScrServer {


    private Queue<byte[]> dataQueue = new LinkedBlockingQueue<byte[]>();
    private Integer PORT;
    private StreamCollector streamCollector;

    public ScrServer(Integer port) {
        this.PORT = port;
    }

    public void start(MyWebSocket socket) {
        streamCollector = new StreamCollector(dataQueue, PORT,socket);
        Thread collector = new Thread(streamCollector);
        collector.start();
        Thread sender = new Thread(new StreamSender(dataQueue,socket));
        sender.start();
    }

}
