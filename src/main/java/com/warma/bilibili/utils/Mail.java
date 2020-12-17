package com.warma.bilibili.utils;

import com.sun.mail.util.MailSSLSocketFactory;
import java.util.Properties;
import javax.mail.Address;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Mail {
    public static boolean sendEmail(String email,String Subject,String... str){
        try {
            Properties props = new Properties();
            // 开启debug调试
            //props.setProperty("mail.debug", "true");
            // 发送服务器需要身份验证
            props.setProperty("mail.smtp.auth", "true");
            // 设置邮件服务器主机名
            props.setProperty("mail.host", "smtp.qq.com");
            // 发送邮件协议名称
            props.setProperty("mail.transport.protocol", "smtp");

            MailSSLSocketFactory sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
            props.put("mail.smtp.ssl.enable", "true");
            props.put("mail.smtp.ssl.socketFactory", sf);

            Session session = Session.getInstance(props);

            MimeMessage msg = new MimeMessage(session);
            msg.setSubject(Subject);
            StringBuilder builder = new StringBuilder();

            for(String s:str){
                builder.append(s);
            }

            msg.setText(builder.toString());
            msg.setFrom(new InternetAddress("2518252697@qq.com","七柠"));

            Transport transport = session.getTransport();
            transport.connect("smtp.qq.com", "2518252697@qq.com", "qodhnvjzrhuodjej");

            transport.sendMessage(msg, new Address[] { new InternetAddress(email)});
            transport.close();
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
}
