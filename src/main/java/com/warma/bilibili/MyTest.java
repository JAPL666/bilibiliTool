package com.warma.bilibili;

import com.warma.bilibili.utils.QRCode;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;

public class MyTest {
    public static void main(String[] args) {
//        QRCode.createConsoleQRCode("http://warma.fun:520");

//        getVideoInfo();

    }
    public static void getVideoInfo() {
        try {
            File file = new File("E:\\1.mp4");
            FileInputStream fileInputStream = new FileInputStream(file);
            FFmpegFrameGrabber fFmpegFrameGrabber = new FFmpegFrameGrabber(fileInputStream);
            fFmpegFrameGrabber.start();

            int lengthInFrames = fFmpegFrameGrabber.getLengthInFrames();
            for (int n = 0; n < lengthInFrames-1; n++) {
                Frame frame = fFmpegFrameGrabber.grabImage();
                BufferedImage bufferedImage = new Java2DFrameConverter().getBufferedImage(frame);


                BufferedImage bi = new BufferedImage(160, 90, BufferedImage.TYPE_3BYTE_BGR);
                bi.getGraphics().drawImage(bufferedImage.getScaledInstance(160, 90, Image.SCALE_SMOOTH), 0, 0, null);

                int width = bi.getWidth();
                int height = bi.getHeight();
                int x=0;
                for (int i = 0; i < height; i++) {
                    for (int j = 0; j < width; j++) {
                        int rgb = bi.getRGB(j, i);
                        int red = new Color(rgb).getRed();
                        if(x>=width){
                            System.out.print("\n");
                            x=0;
                        }

                        String str="▓▓▓";
                        if(red<50){
                            System.out.print("\033[92;1m"+str+"\033[0m");
                        }else if(red<100){
                            System.out.print("\033[91;1m"+str+"\033[0m");
                        }else if(red<150){
                            System.out.print("\033[92;1m"+str+"\033[0m");
                        }else if(red<200){
                            System.out.print("\033[91;1m"+str+"\033[0m");
                        }else if(red<255){
                            System.out.print("\033[92;1m"+str+"\033[0m");
                        }
                        x++;
                    }
                }
                System.out.println();
//                if(n==150){
//                    ImageIO.write(bi, "jpg", new File("C:\\Users\\Administrator\\Desktop\\图片\\"+lengthInFrames+".jpg"));
//                    break;
//                }
            }
            fFmpegFrameGrabber.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
