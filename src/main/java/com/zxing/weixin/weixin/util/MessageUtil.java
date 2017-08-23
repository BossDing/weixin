package com.zxing.weixin.weixin.util;

import com.thoughtworks.xstream.XStream;
import com.zxing.weixin.weixin.po.TextMessge;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageUtil {

    public static final String MESSAGE_TEXT = "text";
    public static final String MESSAGE_IMAGE = "image";
    public static final String MESSAGEXT_VOICE= "voice";
    public static final String MESSAGE_LINK = "link";
    public static final String MESSAGE_VIDEO = "video";
    public static final String MESSAGE_LOCATION = "location";
    public static final String MESSAGE_EVENT = "event";
    public static final String MESSAGE_SUBSCRIBE = "subscribe";
    public static final String MESSAGE_CLICK = "CLICK";
    public static final String MESSAGE_VIEW = "VIEW";

    public static Map<String,String> xmlToMap (HttpServletRequest request)throws DocumentException,IOException {
        Map<String,String> map=new HashMap<>();
        SAXReader reader=new SAXReader();

        InputStream in=request.getInputStream();
        Document doc=reader.read(in);

        Element root=doc.getRootElement();
        List<Element> list=root.elements();

        for(Element element:list){
            map.put(element.getName(),element.getText());
        }

        in.close();
        return map;
    }


    public static String textMessageToXML(TextMessge textMessge){
        XStream xStream=new XStream();
        xStream.alias("xml",textMessge.getClass());//替换根节点
        return xStream.toXML(textMessge);
    }


    public static String initText(String toUserName,String fromUserName,String content){
        TextMessge textMessage=new TextMessge();
        textMessage.setFromUserName(toUserName);
        textMessage.setToUserName(fromUserName);
        textMessage.setMsgType(MessageUtil.MESSAGE_TEXT);
        textMessage.setCreateTime(new Date().getTime()+"");
        textMessage.setContent("您发送的消息是："+content);
        return textMessageToXML(textMessage);
    }

    public static String menuText(){
        StringBuffer sb=new StringBuffer();
        sb.append("欢迎关注~~~");
        sb.append("1,课程介绍");
        sb.append(("2，最新动态"));
        sb.append(("?，帮助"));

        return  sb.toString();
    }


    public static String firstMenuText(){
        StringBuffer sb=new StringBuffer();
        sb.append("课程介绍。。。。。。。。。。。。。");
        return  sb.toString();
    }

    public static String secondMenuText(){
        StringBuffer sb=new StringBuffer();
        sb.append("最新动态。。。。。。。。。。。。。。");
        return  sb.toString();
    }


 }
