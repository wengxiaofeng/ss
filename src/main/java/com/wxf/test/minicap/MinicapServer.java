//package com.wxf.test.minicap;
//
//
//import com.alibaba.fastjson.JSON;
//import org.bytedeco.ffmpeg.global.avcodec;
//import org.bytedeco.javacpp.avcodec;
//import org.bytedeco.javacpp.opencv_core;
//import org.bytedeco.javacv.FFmpegFrameGrabber;
//import org.bytedeco.javacv.FFmpegFrameRecorder;
//import org.bytedeco.javacv.Frame;
//import org.bytedeco.javacv.OpenCVFrameConverter;
//import org.jcodec.codecs.h264.H264Encoder;
//import org.jcodec.codecs.h264.H264Utils;
//import org.jcodec.codecs.h264.encode.H264FixedRateControl;
//import org.jcodec.common.model.ColorSpace;
//import org.jcodec.common.model.Picture;
//
//import javax.imageio.ImageIO;
//import java.awt.image.BufferedImage;
//import java.io.ByteArrayInputStream;
//import java.io.File;
//import java.nio.ByteBuffer;
//
//
///**
// * @Author: chuanpu
// * @Date: 2020-07-15 15:43
// */
//public class MinicapServer {
//
//
//    public static void main(String[] args) throws Exception{
//
//        H264Utils.createMOVSampleEntryFromSpsPpsList(spsList, ppsList)
//        H264Encoder encoder = new H264Encoder(new H264FixedRateControl(100000));
//        RgbToYuv420 transform = new RgbToYuv420(0, 0);
//        byte[] bytes;
//        BufferedImage rgb = ImageIO.read(new ByteArrayInputStream(bytes));
//        Picture yuv = Picture.create(rgb.getWidth(), rgb.getHeight(), ColorSpace.YUV420);
//        transform.transform(AWTUtil.fromBufferedImage(rgb), yuv);
//        ByteBuffer buf = ByteBuffer.allocate(rgb.getWidth() * rgb.getHeight() * 3);
//
//        ByteBuffer ff = encoder.encodeFrame(buf, yuv);
//        sink.write(ff);
//
//        H264Utils.createAvcC();
////        String imagesPath = "";
////        String saveMp4name = "mm.mp4";
////
////        FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(saveMp4name, 640, 480);
//////		recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264); // 28
////        recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264); // 28
//////		recorder.setVideoCodec(avcodec.AV_CODEC_ID_MPEG4); // 13
////        recorder.setFormat("h264");
////        //	recorder.setFormat("mov,mp4,m4a,3gp,3g2,mj2,h264,ogg,MPEG4");
////        recorder.setFrameRate(20);
////        recorder.setPixelFormat(0); // yuv420p
////        recorder.start();
////        //
////        OpenCVFrameConverter.ToIplImage conveter = new OpenCVFrameConverter.ToIplImage();
////        // 列出目录中所有的图片，都是jpg的，以1.jpg,2.jpg的方式，方便操作
////        File file = new File(imagesPath);
////        File[] flist = file.listFiles();
////        // 循环所有图片
////        for (int i = 1; i <= flist.length; i++) {
////            String fname = imagesPath + i + ".jpg";
////            opencv_core.IplImage image = cvLoadImage(fname); // 非常吃内存！！
////            Frame frame = conveter.convert(image);
////            recorder.record(frame);
////            // 释放内存？ cvLoadImage(fname); // 非常吃内存！！
////            opencv_core.cvReleaseImage(image);
////        }
////        recorder.stop();
////        recorder.release();
//    }
//
//
//}
