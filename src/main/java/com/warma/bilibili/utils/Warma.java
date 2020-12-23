package com.warma.bilibili.utils;

import com.warma.bilibili.entity.ResultEntity;

import java.io.*;
import java.math.BigInteger;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

public class Warma {

    //读取文件
    public static String read(String path){
        try {
            FileInputStream in = new FileInputStream(path);
            int lenght;
            byte[] data=new byte[1024];
            ByteArrayOutputStream out=new ByteArrayOutputStream();
            while((lenght=in.read(data))!=-1){
                out.write(data,0,lenght);
            }
            return new String(out.toByteArray(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    //写文件
    public static boolean write(String path,StringBuffer str){
        try {
            FileOutputStream out=new FileOutputStream(path);
            out.write(str.toString().getBytes());
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public static String unicodeDecode(String string) {
        Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");
        Matcher matcher = pattern.matcher(string);
        char ch;
        while (matcher.find()) {
            ch = (char) Integer.parseInt(matcher.group(2), 16);
            string = string.replace(matcher.group(1), ch + "");
        }
        return string;
    }
    //正则表达式
    public static String regex(String regex,String str) {
        StringBuilder string= new StringBuilder();
        Matcher mat= Pattern.compile(regex).matcher(str);
        while(mat.find()) {
            string.append(mat.group(1)).append("\n");
        }
        return string.toString();
    }
    public static ResultEntity post(String url, String string, HashMap<String,String> requestProperty) {
        ResultEntity resultEntity=new ResultEntity();
        try {
            URL url2=new URL(url);
            HttpURLConnection connection=(HttpURLConnection)url2.openConnection();
            connection.setRequestMethod("POST");
            connection.addRequestProperty("Connection", "keep-alive");

            Set<String> keySet = requestProperty.keySet();
            for (String key:keySet){
                connection.addRequestProperty(key,requestProperty.get(key));
            }

            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.connect();

            byte[] data=string.getBytes();
            connection.getOutputStream().write(data);
            int code=connection.getResponseCode();

            if(code==200){


                HashMap<String,String> cookiesMap=new HashMap<>();

                StringBuilder str= new StringBuilder();
                Map<String, List<String>> he = connection.getHeaderFields();
                if(he!=null&&he.get("Set-Cookie")!=null) {
                    for (String s : connection.getHeaderFields().get("Set-Cookie")) {
                        str.append(s.split(";")[0]).append("; ");

                        String[] arays = s.split(";")[0].split("=");
                        cookiesMap.put(arays[0],arays[1]);
                    }
                    resultEntity.setCookieMap(cookiesMap);
                    resultEntity.setCookies(str.toString());
                }

                InputStream is = connection.getInputStream();
                String encoding = connection.getContentEncoding();
                if(encoding!=null){
                    //判断是否gzip
                    if(encoding.equals("gzip")){
                        is=new GZIPInputStream(is);
                    }
                }
                ByteArrayOutputStream message =new ByteArrayOutputStream();
                int lenght;
                byte[] buffer =new byte[1024];
                while((lenght=is.read(buffer))!=-1) {
                    message.write(buffer,0,lenght);
                }
                resultEntity.setResult(new String(message.toByteArray(), StandardCharsets.UTF_8));
                return resultEntity;
            }else{
                System.out.println(code);
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ResultEntity get(String url,HashMap<String,String> requestProperty) {
        ResultEntity resultEntity=new ResultEntity();
        try {
            URL url2=new URL(url);
            HttpURLConnection connection=(HttpURLConnection)url2.openConnection();
            connection.setRequestMethod("GET");
            connection.addRequestProperty("Connection", "keep-alive");

            Set<String> keySet = requestProperty.keySet();
            for (String key:keySet){
                connection.addRequestProperty(key,requestProperty.get(key));
            }

            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.connect();

            int code=connection.getResponseCode();

            if(code==200){

                HashMap<String,String> cookiesMap=new HashMap<>();
                StringBuilder str= new StringBuilder();
                Map<String, List<String>> he = connection.getHeaderFields();
                if(he!=null&&he.get("Set-Cookie")!=null) {
                    for (String s : connection.getHeaderFields().get("Set-Cookie")) {
                        str.append(s.split(";")[0]).append("; ");

                        String[] arays = s.split(";")[0].split("=");
                        cookiesMap.put(arays[0],arays[1]);
                    }
                    resultEntity.setCookies(str.toString());
                    resultEntity.setCookieMap(cookiesMap);
                }

                InputStream is = connection.getInputStream();
                String encoding = connection.getContentEncoding();
                if(encoding!=null){
                    //判断是否gzip
                    if(encoding.equals("gzip")){
                        is=new GZIPInputStream(is);
                    }
                }
                ByteArrayOutputStream message =new ByteArrayOutputStream();
                int lenght;
                byte[] buffer =new byte[1024];
                while((lenght=is.read(buffer))!=-1) {
                    message.write(buffer,0,lenght);
                }
                resultEntity.setResult(new String(message.toByteArray(), StandardCharsets.UTF_8));
                return resultEntity;
            }else{
                System.out.println(code);
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String getMD5String(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            return new BigInteger(1, md.digest()).toString(16);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static String urlEncoder(String str){
        try {
            return URLEncoder.encode(str,"UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    //获取指定范围的随机数
    public static int Random(int s,int d){
        return (int)(Math.random()*(d-s)+s);
    }
    public static String getImageBase64(String imageUrl){
        ByteArrayOutputStream outputStream=new ByteArrayOutputStream();
        try {
            URL url=new URL(imageUrl);
            HttpURLConnection connection=(HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            InputStream inputStream = connection.getInputStream();

            int length=0;
            byte[] data=new byte[1024];
            while((length=inputStream.read(data))!=-1){
                outputStream.write(data,0,length);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        String base64 = Base64.getEncoder().encodeToString(outputStream.toByteArray());
        return "data:image/jpeg;base64,"+base64;
    }
}