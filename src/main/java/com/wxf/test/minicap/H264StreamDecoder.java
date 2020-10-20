//package com.wxf.test.minicap;
//
//import java.awt.image.BufferedImage;
//import java.nio.ByteBuffer;
//
//import us.sosia.media.video.channel.VideoClientChannel.VideoStreamListener;
//import us.sosia.utils.logger.Logger;
//
//import com.xuggle.ferry.IBuffer;
//import com.xuggle.xuggler.ICodec;
//import com.xuggle.xuggler.IPacket;
//import com.xuggle.xuggler.IPixelFormat;
//import com.xuggle.xuggler.IRational;
//import com.xuggle.xuggler.IStreamCoder;
//import com.xuggle.xuggler.IStreamCoder.Direction;
//import com.xuggle.xuggler.IVideoPicture;
//import com.xuggle.xuggler.video.ConverterFactory;
//import com.xuggle.xuggler.video.ConverterFactory.Type;
//import com.xuggle.xuggler.video.IConverter;
//
///**
// * @Author: chuanpu
// * @Date: 2020-07-15 17:21
// */
//public class H264StreamDecoder {
//
//    protected final VideoStreamListener videoStreamListener;
//    protected final IStreamCoder iStreamCoder = IStreamCoder.make(Direction.DECODING, ICodec.ID.CODEC_ID_H264);
//    protected final Type type = ConverterFactory
//            .findRegisteredConverter(ConverterFactory.XUGGLER_BGR_24);
//    public H264StreamDecoder(VideoStreamListener videoStreamListener) {
//        super();
//        this.videoStreamListener = videoStreamListener;
//    }
//    protected volatile long receveiedSize;
//    //add an resizer
//    public void initialize(){
//        //iStreamCoder.setNumPicturesInGroupOfPictures(20);
//        iStreamCoder.setBitRate(250000);
//        iStreamCoder.setBitRateTolerance(9000);
//        iStreamCoder.setPixelType(IPixelFormat.Type.YUV420P);
//        iStreamCoder.setHeight(240);
//        iStreamCoder.setWidth(320);
//        iStreamCoder.setFlag(IStreamCoder.Flags.FLAG_QSCALE, true);
//        iStreamCoder.setGlobalQuality(0);
//        //rate
//        IRational rate = IRational.make(20, 1);
//        iStreamCoder.setFrameRate(rate);
//        //time base
//        //iStreamCoder.setAutomaticallyStampPacketsForStream(true);
//        iStreamCoder.setTimeBase(IRational.make(rate.getDenominator(),rate.getNumerator()));
//        iStreamCoder.open(null, null);
//
//    }
//    /**
//     * pass into the object is an byte buffer data,the decoder will decode it to buffered image
//     * **/
//    public  void decode(ByteBuffer byteBuffer) {
//        if (byteBuffer == null) {
//            throw new NullPointerException("you cannot pass into an null to the decode");
//        }
//
//        int size = byteBuffer.remaining();
//        receveiedSize += size;
//        //start to decode
//        IBuffer iBuffer = IBuffer.make(null, size);
//        IPacket iPacket = IPacket.make(iBuffer);
//        iPacket.getByteBuffer().put(byteBuffer);
//        //decode the packet
//        if (!iPacket.isComplete()) {
//            Logger.SlogHEAD("wrong packet");
//            Logger.SlogDataLine("the h264 packet is not complete","size :"+iPacket.getSize());
//            Logger.SlogBounderLine();
//            return;
//        }
//
//
//        IVideoPicture picture = IVideoPicture.make(IPixelFormat.Type.YUV420P,
//                320, 240);
//        try {
//            // decode the packet into the video picture
//            int postion = 0;
//            int packageSize = iPacket.getSize();
//            while(postion < packageSize){
//                postion+= iStreamCoder.decodeVideo(picture, iPacket, postion);
//                if (postion < 0)
//                    throw new RuntimeException("error "
//                            + " decoding video");
//                // if this is a complete picture, dispatch the picture
//                if (picture.isComplete()){
//                    IConverter  converter = ConverterFactory.createConverter(type
//                            .getDescriptor(), picture);
//                    BufferedImage image = converter.toImage(picture);
//                    //
//                    //BufferedImage convertedImage = ImageUtils.convertToType(image, BufferedImage.TYPE_3BYTE_BGR);
//                    //here ,put out the image
//                    if (videoStreamListener != null) {
//                        videoStreamListener.OnFrame(image);
//                    }
//                    converter.delete();
//                }else{
//                    picture.delete();
//                    iPacket.delete();
//                    return;
//                }
//                //clean the picture and reuse it
//                picture.getByteBuffer().clear();
//            }
//        } finally {
//            if (picture != null)
//                picture.delete();
//            iPacket.delete();
//            // ByteBufferUtil.destroy(data);
//        }
//    }
//
//
//
//
//
//
//
//
//}
