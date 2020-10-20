package com.wxf.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wxf.test.scrcpy.ScrServer;
import lombok.Getter;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
@ServerEndpoint(value = "/display")
public class MyWebSocket {

    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;

    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
    private static CopyOnWriteArraySet<MyWebSocket> webSocketSet = new CopyOnWriteArraySet<MyWebSocket>();

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    @Getter
    private Session session;

    private ScrServer scrcpy;

    private int maxX = 1080;
    private int maxY = 1920;

    public static final int TYPE_INJECT_KEYCODE = 0;
    public static final int TYPE_INJECT_TEXT = 1;
    public static final int TYPE_INJECT_TOUCH_EVENT = 2; //操作
    public static final int TYPE_INJECT_SCROLL_EVENT = 3; //滚动
    public static final int TYPE_BACK_OR_SCREEN_ON = 4;
    public static final int TYPE_EXPAND_NOTIFICATION_PANEL = 5;
    public static final int TYPE_COLLAPSE_NOTIFICATION_PANEL = 6;
    public static final int TYPE_GET_CLIPBOARD = 7;
    public static final int TYPE_SET_CLIPBOARD = 8;
    public static final int TYPE_SET_SCREEN_POWER_MODE = 9;
    public static final int TYPE_ROTATE_DEVICE = 10;

    public static final int ACTION_DOWN             = 0;
    public static final int ACTION_UP               = 1;
    public static final int ACTION_MOVE             = 2;


    /**
     * 连接建立成功调用的方法*/
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        webSocketSet.add(this);     //加入set中
        addOnlineCount();           //在线数加1
        System.out.println("有新连接加入！当前在线人数为" + getOnlineCount());
        scrcpy = new ScrServer(12345);
        scrcpy.start(this);
    }


    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        webSocketSet.remove(this);  //从set中删除
        subOnlineCount();           //在线数减1
        System.out.println("有一连接关闭！当前在线人数为" + getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息*/
    @OnMessage
    public void onMessage(String message, Session session) {

        System.out.println("来自客户端的消息:" + message);
        try {
            OutputStream os = scrcpy.getStreamCollector().getSocketControl().getOutputStream();
            os.write(cmdConvert(message));
            os.flush();
        } catch (Exception e) {

        }
    }

    private byte [] cmdConvert(String msg){
        JSONObject cmd = JSON.parseObject(msg);
        String type = cmd.getString("type");
        if (type.equals("control")){
            String event = cmd.getString("enevt");
            switch(event){
                case "touchDown":
                    return touchDown(cmd.getJSONObject("param"));
                case "touchUp":
                    return touchUp(cmd.getJSONObject("param"));
                case "touchMove":
                    return touchMove(cmd.getJSONObject("param"));
            }
        }
        return "".getBytes();
    }


     @OnError
     public void onError(Session session, Throwable error) {
         System.out.println("发生错误");
        error.printStackTrace();
     }


    public void sendMessage(byte[] buffer) throws IOException {
        ByteBuffer buf = ByteBuffer.wrap(buffer);
        this.session.getBasicRemote().sendBinary(buf);
    }



    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        MyWebSocket.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        MyWebSocket.onlineCount--;
    }

    private byte [] touchDown(JSONObject cmd){
        Double x = cmd.getDouble("x") * this.maxX;
        Double y = cmd.getDouble("y") * this.maxY;

        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(bos);
            dos.writeByte(TYPE_INJECT_TOUCH_EVENT);
            dos.writeByte(ACTION_DOWN);
            dos.writeLong(-42); // pointerId
            dos.writeInt(x.intValue());
            dos.writeInt(y.intValue());
            dos.writeShort(maxX);
            dos.writeShort(maxY);
            dos.writeShort(0xffff); // pressure
            dos.writeInt(1);
            byte[] packet = bos.toByteArray();
            return packet;
        } catch (Exception e) {

        }

        return null;

    }

    private byte [] touchUp(JSONObject cmd){
        Double x = cmd.getDouble("x") * this.maxX;
        Double y = cmd.getDouble("y") * this.maxY;

        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(bos);
            dos.writeByte(TYPE_INJECT_TOUCH_EVENT);
            dos.writeByte(ACTION_UP);
            dos.writeLong(-42); // pointerId
            dos.writeInt(x.intValue());
            dos.writeInt(y.intValue());
            dos.writeShort(maxX);
            dos.writeShort(maxY);
            dos.writeShort(0); // pressure
            dos.writeInt(1);
            byte[] packet = bos.toByteArray();
            return packet;
        } catch (Exception e) {

        }

        return null;

    }


    private byte [] touchMove(JSONObject cmd){
        Double x = cmd.getDouble("x") * this.maxX;
        Double y = cmd.getDouble("y") * this.maxY;

        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(bos);
            dos.writeByte(TYPE_INJECT_TOUCH_EVENT);
            dos.writeByte(ACTION_MOVE);
            dos.writeLong(-42); // pointerId
            dos.writeInt(x.intValue());
            dos.writeInt(y.intValue());
            dos.writeShort(maxX);
            dos.writeShort(maxY);
            dos.writeShort(0xffff); // pressure
            dos.writeInt(1);
            byte[] packet = bos.toByteArray();
            return packet;
        } catch (Exception e) {

        }

        return null;
    }


    public static void main(String[] args) {
        Double i = 1.11;
        int j = i.intValue();
        System.out.println(i.intValue());
        System.out.println(j);

    }

}
