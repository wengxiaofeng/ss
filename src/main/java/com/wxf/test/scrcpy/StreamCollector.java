package com.wxf.test.scrcpy;

import com.wxf.test.MyWebSocket;
import lombok.Data;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;

@Data
public class StreamCollector implements Runnable {

    private InputStream stream = null;
    private Socket socketVideo;
    private Socket socketControl;
    private Process processAdbForward;
    private static final int BUFFER_SIZE = 1024 * 1024;
    private static final int READ_BUFFER_SIZE = 1024 * 5;
    private Queue<byte[]> dataQueue;
    private Integer PORT;
    private MyWebSocket socket;

    public StreamCollector(Queue<byte[]> dataQueue, Integer port, MyWebSocket socket) {
        this.dataQueue = dataQueue;
        this.PORT = port;
        this.socket = socket;
    }

    public void run() {
        try {
//            try {
//
//                Runtime.getRuntime().exec("adb push scrcpy-server /data/local/tmp/scrcpy-server.jar");
//                Runtime.getRuntime().exec(String.format("adb forward tcp:%d localabstract:scrcpy", PORT));
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            System.out.println("包push手机");
//
////                         SCRCPY_VERSION, 1版本号
////                        log_level_to_server_string(params->log_level), 2日志级别
////                        max_size_string, 3最大size
////                        bit_rate_string,
////                        max_fps_string, 5帧率
////                        lock_video_orientation_string, 0shuping/1hengping
////                        server->tunnel_forward ? "true" : "false",
////                        params->crop ? params->crop : "-", 图片大小
////                        "true", // always send frame meta (packet boundaries + timestamp)
////                        params->control ? "true" : "false", 控制
////                        display_id_string,
////                        params->show_touches ? "true" : "false",
////                        params->stay_awake ? "true" : "false",
////                        params->codec_options ? params->codec_options : "-",
//
//            CompletableFuture.runAsync(()->{
//                try{
////                            bitrate = 2 * width * height * framerate ;//码率 124416000
////                            processAdbForward =  Runtime.getRuntime().exec("adb shell CLASSPATH=/data/local/tmp/scrcpy-server.jar app_process / com.genymobile.scrcpy.Server 0 800000/124416000 true - true false"); 1080:1920:0:0
//                    processAdbForward =  Runtime.getRuntime().exec("adb shell CLASSPATH=/data/local/tmp/scrcpy-server.jar app_process / com.genymobile.scrcpy.Server 1.14 info 0 12441600 20 -1 true 1080:1920:0:0 false true 0 false false -");
//
//                    processAdbForward.waitFor();
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            });
//
//            try {
//                Thread.sleep(1 * 1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }


            socketVideo = new Socket("localhost", PORT);
//            socketControl = new Socket("localhost", PORT);
            stream = socketVideo.getInputStream();

            int readLength;
            int naluIndex = 0;
            int bufferLength = 0;

            byte[] buffer = new byte[BUFFER_SIZE];


            while (socketVideo.isConnected() && socket.getSession().isOpen()) {
                readLength = stream.read(buffer, bufferLength, READ_BUFFER_SIZE);
                if(readLength>0) {
//                    System.out.println("读取到数据");
                    bufferLength += readLength;
                    for (int i = 5; i < bufferLength - 4; i++) {
                        if (buffer[i] == 0x00 &&
                                buffer[i + 1] == 0x00 &&
                                buffer[i + 2] == 0x00 &&
                                buffer[i + 3] == 0x01
                        ){
                            naluIndex = i;

                            byte[] naluBuffer = new byte[naluIndex];
                            System.arraycopy(buffer, 0, naluBuffer, 0, naluIndex);
                            dataQueue.add(naluBuffer);
                            bufferLength -= naluIndex;
                            System.arraycopy(buffer, naluIndex, buffer, 0, bufferLength);
                            i = 5;
                        }
                    }

                }
            }

            processAdbForward.destroy();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (socketVideo != null && socketVideo.isConnected()) {
                try {
                    socketVideo.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (socketControl != null && socketControl.isConnected()) {
                try {
                    socketControl.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

}
