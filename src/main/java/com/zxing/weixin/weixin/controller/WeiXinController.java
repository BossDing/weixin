package com.zxing.weixin.weixin.controller;

import com.zxing.weixin.weixin.po.TextMessge;
import com.zxing.weixin.weixin.util.CheckUtil;
import com.zxing.weixin.weixin.util.MessageUtil;
import org.dom4j.DocumentException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.nio.cs.ext.MacUkraine;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Map;

@RestController
public class WeiXinController {


    //接入URL  ngrok映射至公网
    @RequestMapping("/weixin")
    public void  weixin(HttpServletRequest request , HttpServletResponse response){//@PathParam("echostr") echostr
        String signature=request.getParameter("signature");
        String timestamp=request.getParameter("timestamp");
        String nonce=request.getParameter("nonce");
        String echostr=request.getParameter("echostr");
        PrintWriter out=null;
        try{
            out=response.getWriter();
            if(CheckUtil.checkSignature(signature,timestamp,nonce)){
                out.print(echostr);
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if (out!=null){
                out.close();
            }
        }
    }



    @PostMapping("/response")
    public void response(HttpServletRequest request , HttpServletResponse response)throws IOException{
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");

        Map<String,String> map=null;
        try{
            map= MessageUtil.xmlToMap(request);
        }catch (DocumentException e){
            e.printStackTrace();
        }

        String toUserName=map.get("ToUserName");
        String fromUserName=map.get("fromUserName");
        String msgType=map.get("MsgType");
        String content=map.get("Content");

        String message=null;
        //文本消息处理
        if (MessageUtil.MESSAGE_TEXT.equals(msgType)){
            if("1".equals(content)){
                message=MessageUtil.initText(toUserName,fromUserName,MessageUtil.firstMenuText());
            }
            if("2".equals(content)){
                message=MessageUtil.initText(toUserName,fromUserName,MessageUtil.secondMenuText());
            }
        }
        //事件处理
        else if(MessageUtil.MESSAGE_EVENT.equals(msgType)){
            String eventType=map.get("Event");
            //关注
            if(MessageUtil.MESSAGE_SUBSCRIBE.equals(eventType)){
                message=MessageUtil.initText(toUserName,fromUserName,MessageUtil.menuText());
            }
        }
        //其他类型类似

        PrintWriter out=response.getWriter();
        out.print(message);

        if (out!=null) out.close();
    }
}
