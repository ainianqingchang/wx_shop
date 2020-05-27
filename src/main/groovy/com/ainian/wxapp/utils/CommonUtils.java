package com.ainian.wxapp.utils;

import com.alibaba.fastjson.JSONObject;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.util.*;

public class CommonUtils {

    public static JSONObject resJson(int code, String errmsg) {
        JSONObject json = new JSONObject();
        json.put("errcode", code);
        json.put("errmsg", errmsg);
        return json;
    }

    public static String getRandomString(int length) {
        String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random random=new Random();
        StringBuffer sb=new StringBuffer();
        for(int i=0;i<length;i++){
            int number=random.nextInt(52);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    public static String makeOrderNo() {
        String str="ABCDEFGHIJ";
        String hex="123456789ABCDEF";
        Random random=new Random();
        int rInt = random.nextInt(89);
        rInt+= 10;
        StringBuffer sb=new StringBuffer();

        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month=Calendar.getInstance().get(Calendar.MONTH)+1;
        sb.append(str.charAt(year-2017));
        sb.append(hex.charAt(month));
        long l = System.currentTimeMillis();
        sb.append(String.valueOf(l));
        sb.append(String.valueOf(rInt));
        return sb.toString();
    }


    public static Map<String, String> xmlToMap(String xml, String charset) throws UnsupportedEncodingException, DocumentException, DocumentException {

        Map<String, String> respMap = new HashMap<String, String>();

        SAXReader reader = new SAXReader();
        Document doc = reader.read(new ByteArrayInputStream(xml.getBytes(charset)));
        Element root = doc.getRootElement();
        xmlToMap(root, respMap);
        return respMap;
    }

    public static Map<String, String> xmlToMap(Element tmpElement, Map<String, String> respMap){
        ArrayList<String> strings = new ArrayList<String>();
        for (String s:strings
        ) {

        }
        if (tmpElement.isTextOnly()) {
            respMap.put(tmpElement.getName(), tmpElement.getText());
            return respMap;
        }

        @SuppressWarnings("unchecked")
        Iterator<Element> eItor = tmpElement.elementIterator();
        while (eItor.hasNext()) {
            Element element = eItor.next();
            xmlToMap(element, respMap);
        }
        return respMap;
    }



}
