package com.warma.bilibili;

import com.warma.bilibili.utils.BiLiBiLiApi;
import com.warma.bilibili.utils.QRCode;

public class MyTest {
    public static void main(String[] args) {
//        QRCode.createConsoleQRCode("http://warma.fun:520");

        BiLiBiLiApi biLiBiLiApi = new BiLiBiLiApi();
        String s = biLiBiLiApi.getInfo(2).toString();
        System.out.println(s);

    }
}
