package com.zxing.weixin.weixin.util;

import java.util.Arrays;

public class CheckUtil {
    private static final String token="zxing";
    public static boolean checkSignature(String signature,String timestamp ,String nonce){
        String[] arr=new String[]{token,timestamp,nonce};
        //排序
        Arrays.sort(arr);
        //生成字符窜
        StringBuffer content=new StringBuffer();
        for (int i=0;i<arr.length;i++){
            content.append(arr[i]);
        }
        //加密
        String temp=SHA1.getSha1(content.toString());

        return temp.equals(signature);
    }
}
