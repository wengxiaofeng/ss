//package com.wxf.test.minicap;
//
//
//import java.awt.image.BufferedImage;
//import java.nio.ByteBuffer;
//
//import com.xuggle.xuggler.ICodec;
//import com.xuggle.xuggler.IPacket;
//import com.xuggle.xuggler.IPixelFormat.Type;
//import com.xuggle.xuggler.IRational;
//import com.xuggle.xuggler.IStreamCoder;
//import com.xuggle.xuggler.IStreamCoder.Direction;
//import com.xuggle.xuggler.IVideoPicture;
//import com.xuggle.xuggler.video.ConverterFactory;
//import com.xuggle.xuggler.video.IConverter;
//import org.xmind.core.style.IStyleSheet;
//
///**
// * @Author: chuanpu
// * @Date: 2020-07-15 17:29
// */
//public class H264StreamEncoder {
//
//    protected final IStreamCoder iStreamCoder = IStreamCoder.make(Direction.ENCODING, ICodec.ID.CODEC_ID_H264);
//    protected final IPacket iPacket = IPacket.make();
//    protected long startTime ;
//    protected H264StreamEncodeListener encodeListener;
//    public H264StreamEncoder() {
//        super();
//    }
//
//    public interface H264StreamEncodeListener {
//        public void onKeyFrame(ByteBuffer byteBuffer);
//    }
//
//
//
//    public H264StreamEncodeListener getEncodeListener() {
//        return encodeListener;
//    }
//
//
//    public void setEncodeListener(H264StreamEncodeListener encodeListener) {
//        this.encodeListener = encodeListener;
//    }
//
//
//    public void initialize(){
//        //setup
//        iStreamCoder.setNumPicturesInGroupOfPictures(20);
//        iStreamCoder.setBitRate(250000);
//        iStreamCoder.setBitRateTolerance(9000);
//        iStreamCoder.setPixelType(Type.YUV420P);
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
//        //open it
//        int revl = iStreamCoder.open(null, null);
//        if (revl < 0) {
//            throw new RuntimeException("could not open the coder");
//        }
//    }
//
//
//    /***
//     * passed in the buffered image,and then encode it to an H264 packet object
//     * ,if the packet is complete ,then return the byte buffer data,otherwise return null;
//     * */
//    @Override
//    public Object encode(Object obj) {
//        if (obj == null) {
//            throw new NullPointerException("you cannot pass in an null~~");
//        }
//        if (!(obj instanceof BufferedImage)) {
//            throw new IllegalArgumentException("your need to pass into an bufferedimage");
//        }
//
//        BufferedImage bufferedImage = (BufferedImage)obj;
//        //here is the encode
//        //convert the image
//        BufferedImage convetedImage = ImageUtils.convertToType(bufferedImage, BufferedImage.TYPE_3BYTE_BGR);
//        IConverter converter = ConverterFactory.createConverter(convetedImage, Type.YUV420P);
//        //to frame
//        long now = System.currentTimeMillis();
//        if (startTime == 0) {
//            startTime = now;
//        }
//        IVideoPicture pFrame = converter.toPicture(convetedImage, (now - startTime)*1000);
//        //pFrame.setQuality(0);
//        iStreamCoder.encodeVideo(iPacket, pFrame, 0) ;
//        //free the MEM
//        pFrame.delete();
//        converter.delete();
//        //write to the container
//        if (iPacket.isComplete()) {
//            //container.writePacket(iPacket);
//            //should free the data?
//            //iPacket.delete();
//            //here we send the package to the remote peer
//            //System.out.println("package is ok");
//            try{
//                ByteBuffer byteBuffer = iPacket.getByteBuffer();
//
//                ByteBuffer copy = ByteBuffer.allocate(byteBuffer.limit());
//                copy.put(byteBuffer);
//                copy.flip();
//                if (iPacket.isKeyPacket()) {
//                    if (encodeListener != null) {
//                        encodeListener.onKeyFrame(copy);
//                    }
//                }
//
//                return copy;
//            }finally{
//                iPacket.reset();
//            }
//        }else{
//            System.out.println("package not ok");
//        }
//
//        return null;
//    }
//}
