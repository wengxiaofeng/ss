服务端口号：默认8888，Application里面可以修改

scrcpy-server在根目录下，版本1.14

scrcpy adb forward 端口号默认27183 可以在MyWebSocket中修改

##代码
MyWebSocket 前后端websocket
ScrServer scrcpy操作入口类
StreamCollector 收集视频流，切割，放入队列
StreamSender 队列中取数据，发送


#流程
##push包到手机，forward端口，执行命令
* `adb push scrcpy-server /data/local/tmp/scrcpy-server.jar`
* `adb forward tcp:27183 localabstract:scrcpy`
* `adb shell CLASSPATH=/data/local/tmp/scrcpy-server.jar app_process / com.genymobile.scrcpy.Server 1.14 info 0 12441600 20 -1 true 1080:1920:0:0 false true 0 false false -`

##创建2个socket，建立视频和操作链接
* `socketVideo = new Socket("localhost", PORT);`
* `socketControl = new Socket("localhost", PORT);`

电脑模拟，监听端口
* nc localhost 27183 //第一个视频流。如果没有数据，说明前3步有问题
* nc localhost 27183 //操作

测试了一些机型，兼容的
* 小米note3
* 华为mate9
* iqoo
* 1加3T
* vivox20 plus


