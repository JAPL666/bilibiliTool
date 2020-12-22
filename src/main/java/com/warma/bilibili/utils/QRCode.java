package com.warma.bilibili.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.ByteArrayOutputStream;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Base64;

public class QRCode {
    public static void createQRCodeImage(String text, int width, int height, String filePath){
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

            Path path = FileSystems.getDefault().getPath(filePath);
            MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static String createQRCodeImageBase64(String text, int width, int height){
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

            ByteArrayOutputStream data=new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", data);
            String base64 = Base64.getEncoder().encodeToString(data.toByteArray());
            return "data:image/jpeg;base64,"+base64;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
